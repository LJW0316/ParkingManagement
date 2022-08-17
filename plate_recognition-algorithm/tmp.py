import shutil
from pathlib import Path
from data.load_data import CHARS, CHARS_DICT
from models.LPRNet import build_lprnet
from torch.autograd import Variable
import argparse
import time
import numpy as np
import random
import os
from collections import Counter
import cv2
import json
from urllib.parse import quote
import torch
import torch.backends.cudnn as cudnn
from numpy import random
import requests
from models.experimental import attempt_load
from utils.datasets import LoadStreams, LoadImages
from utils.general import (
    check_img_size, non_max_suppression, apply_classifier, scale_coords,
    xyxy2xywh, plot_one_box, strip_optimizer, set_logging)
from utils.torch_utils import select_device, load_classifier, time_synchronized

def detect(save_img=False):
    out, source, weights, view_img, save_txt, imgsz = \
        opt.save_dir, opt.source, opt.weights, opt.view_img, opt.save_txt, opt.img_size
    webcam = source.isnumeric() or source.startswith(('rtsp://', 'rtmp://', 'http://')) or source.endswith('.txt')

    resultList=[]

    # Initialize
    set_logging()
    device = select_device(opt.device)
    if os.path.exists(out):  # output dir
        shutil.rmtree(out)  # delete dir
    os.makedirs(out)  # make new dir
    half = device.type != 'cpu'  # half precision only supported on CUDA

    # Load model
    model = attempt_load(weights, map_location=device)  # load FP32 model
    imgsz = check_img_size(imgsz, s=model.stride.max())  # check img_size
    if half:
        model.half()  # to FP16

    # Second-stage classifier
    classify = False
    if classify:
        modelc = load_classifier(name='resnet101', n=2)  # initialize
        modelc.load_state_dict(torch.load('weights/resnet101.pt', map_location=device)['model'])  # load weights
        modelc.to(device).eval()

    # Set Dataloader
    vid_path, vid_writer = None, None
    if webcam:
        view_img = True
        cudnn.benchmark = True  # set True to speed up constant image size inference
        dataset = LoadStreams(source, img_size=imgsz)
    else:
        save_img = True
        dataset = LoadImages(source, img_size=imgsz)

    # Get names and colors
    names = model.module.names if hasattr(model, 'module') else model.names
    colors = [[random.randint(0, 255) for _ in range(3)] for _ in range(len(names))]

    # Run inference
    t0 = time.time()
    img = torch.zeros((1, 3, imgsz, imgsz), device=device)  # init img
    _ = model(img.half() if half else img) if device.type != 'cpu' else None  # run once
    for path, img, im0s, vid_cap in dataset:
        img = torch.from_numpy(img).to(device)
        img = img.half() if half else img.float()  # uint8 to fp16/32
        img /= 255.0  # 0 - 255 to 0.0 - 1.0
        if img.ndimension() == 3:
            img = img.unsqueeze(0)

        # Inference
        t1 = time_synchronized()
        pred = model(img, augment=opt.augment)[0]

        # Apply NMS
        pred = non_max_suppression(pred, opt.conf_thres, opt.iou_thres, classes=opt.classes, agnostic=opt.agnostic_nms)
        t2 = time_synchronized()

        # Apply Classifier
        if classify:
            pred = apply_classifier(pred, modelc, img, im0s)

        # Process detections
        for i, det in enumerate(pred):  # detections per image
            if webcam:  # batch_size >= 1
                p, s, im0 = path[i], '%g: ' % i, im0s[i].copy()
            else:
                p, s, im0 = path, '', im0s

            save_path = str(Path(out) / Path(p).name)
            txt_path = str(Path(out) / Path(p).stem) + ('_%g' % dataset.frame if dataset.mode == 'video' else '')
            s += '%gx%g ' % img.shape[2:]  # print string
            gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
            if det is not None and len(det):
                # Rescale boxes from img_size to im0 size
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()

                # Print results
                for c in det[:, -1].unique():
                    n = (det[:, -1] == c).sum()  # detections per class
                    s += '%g %ss, ' % (n, names[int(c)])  # add to string

                # Write results
                for *xyxy, conf, cls in reversed(det):
                    if save_txt:  # Write to file
                        print("没有转换的坐标:")
                        print(xyxy)
                        xywh = (xyxy2xywh(torch.tensor(xyxy).view(1, 4)) / gn).view(-1).tolist()  # normalized xywh
                        print("归一化后的坐标:")
                        print(xywh)
                        line = (cls, conf, *xywh) if opt.save_conf else (cls, *xywh)  # label format
                        with open(txt_path + '.txt', 'a') as f:
                            f.write(('%g ' * len(line) + '\n') % line)

                    if save_img or view_img:  # Add bbox to image
                        crop = im0s[int(xyxy[1].item()):int(xyxy[3].item()), int(xyxy[0].item()):int(xyxy[2].item())]
                        para = test(crop)
                        label = '%s %.2f' % (names[int(cls)], conf)+para
                        im0=plot_one_box(xyxy, im0, label=label, color=colors[int(cls)], line_thickness=3)
                        resultList.append(para)



            
    if save_txt or save_img:
        print('Results saved to %s' % Path(out))

    print('Done. (%.3fs)' % (time.time() - t0))
    print(para)
    StoreResult(resultList)


def StoreResult(resultList):
    # header = {
    #     "Accept": "*/*",
    #     "Accept-Encoding": "gzip, deflate, br",
    #     "Accept-Language": "zh-CN,zh;q=0.9",
    #     "Connection": "keep-alive",
    #     "Content-Type": "application/json",
    #     "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"
    # }
    # number = Counter(resultList)
    # result = number.most_common()
    # Data=quote(result[0][0].encode('utf-8'))
    # dic={}
    # dic['plate']=Data
    # post_json= json.dumps(dic)
    #r=requests.post("http://47.103.85.74:8090/api/parking/in_car",data=post_json,headers=header)
    number = Counter(resultList)
    result = number.most_common()
    data=result[0][0]
    args = get_parser()
    if(args.plate):
        #url="http://47.103.85.74:8090/api/parking/in_car?plate="+data
        #r=requests.post(url)
        print("入库")
    else:
        #url="http://47.103.85.74:8090/api/parking/out_car?plate="+data
        #r=requests.post(url)
        print("出库")
    
    

def get_parser():
    parser = argparse.ArgumentParser(description='parameters to train net')
    parser.add_argument('--img_size2', default=[94, 24], help='the image size')
    parser.add_argument('--test_img_dirs2', default="./data/test/", help='the test images path')
    parser.add_argument('--dropout_rate2', default=0, help='dropout rate.')
    parser.add_argument('--lpr_max_len2', default=8, help='license plate number max length.')
    parser.add_argument('--test_batch_size2', default=1, help='testing batch size.')
    parser.add_argument('--phase_train2', default=False, type=bool, help='train or test phase flag.')
    parser.add_argument('--num_workers2', default=8, type=int, help='Number of workers used in dataloading')
    parser.add_argument('--cuda2', default=True, type=bool, help='Use cuda to train model')
    parser.add_argument('--show2', default=True, type=bool, help='show test image and its predict result or not.')
    parser.add_argument('--pretrained_model2', default='./weights/Final_LPRNet_model.pth', help='pretrained base model')
    parser.add_argument('--weights', default=' ', help='do not use')
    parser.add_argument('--conf', default=' ', help='do not use')
    parser.add_argument('--source', default=' ', help='do not use')
    parser.add_argument('--plate', default='1', help='car in or out')


    args = parser.parse_args()

    return args

def collate_fn(batch):
    imgs = []
    labels = []
    lengths = []
    for _, sample in enumerate(batch):
        img, label, length = sample
        imgs.append(torch.from_numpy(img))
        labels.extend(label)
        lengths.append(length)
    labels = np.asarray(labels).flatten().astype(np.float32)

    return (torch.stack(imgs, 0), torch.from_numpy(labels), lengths)

def test(images):
    args = get_parser()

    lprnet = build_lprnet(lpr_max_len=args.lpr_max_len2, phase=args.phase_train2, class_num=len(CHARS), dropout_rate=args.dropout_rate2)
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    lprnet.to(device)

    # load pretrained model
    if args.pretrained_model2:
        #lprnet.load_state_dict(torch.load(args.pretrained_model2))
        lprnet.load_state_dict(torch.load(args.pretrained_model2, map_location=torch.device('cpu')))
    else:
        return False

    try:
        para=Greedy_Decode_Eval(lprnet, images, args)
        return para
    finally:
        cv2.destroyAllWindows()

def Greedy_Decode_Eval(Net, img, args):
    # TestNet = Net.eval()
    t1 = time.time()
    height, width, _ = img.shape

    if height != args.img_size2[1] or width != args.img_size2[0]:
        img = cv2.resize(img, args.img_size2)


    images = img.astype('float32')
    images -= 127.5
    images *= 0.0078125
    images = np.transpose(images, (2, 0, 1))

    images = torch.tensor(images)

    images = images[None]

    if args.cuda2:
        #!!!!!
        #images = Variable(images.cuda())
        images = Variable(images.to(device))
    else:
        images = Variable(images)

    # forward
    prebs = Net(images)
    # greedy decode
    prebs = prebs.cpu().detach().numpy()
    preb_labels = list()
    for i in range(prebs.shape[0]):
        preb = prebs[i, :, :]
        preb_label = list()
        for j in range(preb.shape[1]):
            preb_label.append(np.argmax(preb[:, j], axis=0))
        no_repeat_blank_label = list()
        pre_c = preb_label[0]
        if pre_c != len(CHARS) - 1:
            no_repeat_blank_label.append(pre_c)
        for c in preb_label: # dropout repeate label and blank label
            if (pre_c == c) or (c == len(CHARS) - 1):
                if c == len(CHARS) - 1:
                    pre_c = c
                continue
            no_repeat_blank_label.append(c)
            pre_c = c
        preb_labels.append(no_repeat_blank_label)
    lb = ""
    for i in preb_labels[0]:
        lb += CHARS[i]
    return lb


if __name__ == '__main__':
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    parser = argparse.ArgumentParser()
    parser.add_argument('--weights', nargs='+', type=str, default='yolov5s.pt', help='model.pt path(s)')
    parser.add_argument('--source', type=str, default='inference/images', help='source')  # file/folder, 0 for webcam
    parser.add_argument('--img-size', type=int, default=640, help='inference size (pixels)')
    parser.add_argument('--conf-thres', type=float, default=0.25, help='object confidence threshold')
    parser.add_argument('--iou-thres', type=float, default=0.45, help='IOU threshold for NMS')
    parser.add_argument('--device', default='cpu', help='cuda device, i.e. 0 or 0,1,2,3 or cpu')
    parser.add_argument('--view-img', action='store_true', help='display results')
    parser.add_argument('--save-txt', action='store_true', help='save results to *.txt')
    parser.add_argument('--save-conf', action='store_true', help='save confidences in --save-txt labels')
    parser.add_argument('--save-dir', type=str, default='inference/output', help='directory to save results')
    parser.add_argument('--classes', nargs='+', type=int, help='filter by class: --class 0, or --class 0 2 3')
    parser.add_argument('--agnostic-nms', action='store_true', help='class-agnostic NMS')
    parser.add_argument('--augment', action='store_true', help='augmented inference')
    parser.add_argument('--update', action='store_true', help='update all models')
    parser.add_argument('--plate', default='1', help='car in or out')

    opt = parser.parse_args()
    print(opt)

    with torch.no_grad():
        if opt.update:  # update all models (to fix SourceChangeWarning)
            for opt.weights in ['yolov5s.pt', 'yolov5m.pt', 'yolov5l.pt', 'yolov5x.pt']:
                detect()
                strip_optimizer(opt.weights)
        else:
            detect()
