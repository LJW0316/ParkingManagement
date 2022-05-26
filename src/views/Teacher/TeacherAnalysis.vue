<template>
  <div class="map">
    <div id="main" style="width: 100%; height: 400px;">
    </div>
  </div>
</template>
<script>
import {onMounted, reactive} from "vue";
import * as echarts from 'echarts';
import {localGet} from "@/utils";
import axios from "@/utils/axios";
export default {
  name: "TeacherMain",
  setup(){
    const tno=localGet('token').id
    const state = reactive({
      map:[
        {
          names:"4.0",
          count:0
        },
        {
          names:"3.7",
          count:0
        },
        {
          names:"3.3",
          count:0
        },
        {
          names:"3.0",
          count:0
        },
        {
          names:"2.7",
          count:0
        },
        {
          names:"2.3",
          count:0
        },
        {
          names:"2.0",
          count:0
        },
        {
          names:"1.7",
          count:0
        },
        {
          names:"1.5",
          count:0
        },
        {
          names:"1.0",
          count:0
        },
        {
          names:"0.0",
          count:0
        },
      ],
      tableData: []// 数据列表
    })
    const getCno = () =>{
      axios.get('teacher/chart', {
        params: {
          tno: tno
        }
      }).then(res => {
        state.tableData=res
      })
    }
    const echartInit = () =>{
      var myChart = echarts.init(document.getElementById('main'));
      // 指定图表的配置项和数据
      var option = {
        title: {
          text: 'ECharts 入门示例'
        },
        tooltip: {},
        legend: {
          data:['销量']
        },
        xAxis: {
          data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
        },
        yAxis: {},
        series: [{
          name: '销量',
          type: 'bar',
          data: [5, 20, 36, 10, 10, 20]
        }]
      };
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }
    //onMounted
    onMounted(()=>{
      getCno()
      echartInit()
    })
    //return
    return {
      echartInit,
      getCno,
      state,
    };
  }
}
</script>

<style scoped>

</style>