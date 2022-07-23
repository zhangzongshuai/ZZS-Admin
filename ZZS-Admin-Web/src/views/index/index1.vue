<template>
  <div class="home">
    <!--    <TheCharts id="chart" :style="{width: '600px',height:'400px'}" :option="option"></TheCharts>-->
    <el-row :gutter="10">
      <el-col :span="16">
        <el-card class="box-card" shadow="never" body-style="padding:20px 10px 0 10px">
          <TheCharts id="chart1" :style="{width: '100%',height:'300px'}" :option="finishedRateOption"></TheCharts>
        </el-card>
      </el-col>
      <!--      <el-col :span="8">-->
      <!--        <div style="height: 300px">-->
      <!--          <TheCharts id="chart2" :style="{width: '100%',height:'100%'}" :option="option"></TheCharts>-->
      <!--        </div>-->
      <!--      </el-col>-->
      <el-col :span="8">
        <el-card class="box-card" shadow="never">
          <div>
            <div
                style="background-color: #66b1ff;height: 40px;line-height: 40px;font-weight: bold;font-size: 18px;text-align: center">
              自销品价格一览
            </div>
            <div id="topLeft" style="height: 240px;overflow:hidden">
              <div id="product1">
              </div>
              <div id="product2"></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 10px" :gutter="10">
      <!--      <el-col :span="8">-->
      <!--        <div style="height: 300px">-->
      <!--          <TheCharts id="chart4" :style="{width: '100%',height:'100%'}" :option="option"></TheCharts>-->
      <!--        </div>-->
      <!--      </el-col>-->
      <el-col :span="16">
        <el-card class="box-card" shadow="never">
          <el-table
              :data="tableData"

              :height="350"
              style="width: 100%"
              :cell-style="{padding:'10px 0'}"
              :row-style="rowStyle">
            <el-table-column
                prop="name"
                label="油品"
                width="120">
              <!--                <template slot-scope="scope">-->
              <!--          <span v-if="scope.row.name!='汽油'&& scope.row.name !='柴油'">-->
              <!--                      <el-button @click="detailPlan(scope.row)" type="text"-->
              <!--                                 size="small">{{ scope.row.name }}</el-button>-->
              <!--          </span>-->
              <!--                  <span v-else>-->
              <!--            {{ scope.row.name }}-->
              <!--          </span>-->

              <!--                </template>-->
            </el-table-column>
            <el-table-column
                prop="lastMonthCq"
                label="上月超欠"
                width="80">
            </el-table-column>
            <el-table-column
                prop="plan"
                label="月度计划"
                width="120">
            </el-table-column>
            <el-table-column
                prop="beforeAmount"
                label="前期完成量"
                width="120">
            </el-table-column>
            <el-table-column
                prop="dayAmount"
                label="当日完成量"
                width="120">
            </el-table-column>
            <el-table-column
                prop="totalAmount"
                label="累计完成量"
                width="120">
            </el-table-column>
            <el-table-column
                prop="cql"
                label="计划进度超欠"
                width="120">
            </el-table-column>
            <el-table-column
                prop="rate"
                label="完成率"
            >
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">

        <el-card class="box-card" shadow="never">
          <div style="height: 350px">
            <TheCharts id="chart6" :style="{width: '100%',height:'100%'}" :option="totalRateOption"></TheCharts>
          </div>
        </el-card>

      </el-col>
    </el-row>
    <!--    <el-row>-->
    <!--      <el-col :span="8">-->
    <!--        <div style="height: 300px">-->
    <!--          <TheCharts id="chart7" :style="{width: '100%',height:'100%'}" :option="option"></TheCharts>-->
    <!--        </div>-->
    <!--      </el-col>-->
    <!--      <el-col :span="8">-->
    <!--        <div style="height: 300px">-->
    <!--          <TheCharts id="chart8" :style="{width: '100%',height:'100%'}" :option="option"></TheCharts>-->
    <!--        </div>-->
    <!--      </el-col>-->
    <!--      <el-col :span="8">-->
    <!--        <div style="height: 300px">-->
    <!--          <TheCharts id="chart9" :style="{width: '100%',height:'100%'}" :option="option"></TheCharts>-->
    <!--        </div>-->
    <!--      </el-col>-->
    <!--    </el-row>-->
  </div>
</template>

<script>
import TheCharts from '@/components/TheCharts';
import {formatDate} from "@/common";

export default {
  name: "index",
  data() {
    return {
      option: {},
      tableData: [],
      finishedRateOption: {},
      totalRateOption: {
        graphic: [{
          type: 'text',
          left: '20%',
          top: '60%',
          z: 100,
          style: {
            fill: '#fff',
            text: '汽油完成率',
            font: '12px Microsoft YaHei'
          },
          // children: [{
          //   type: 'text',
          //   z: 100,
          //   left: '10',
          //   top: 'middle',
          //   style: {
          //     fill: '#fff',
          //     text: '汽油',
          //     font: '12px Microsoft YaHei'
          //   }
          // }]
        },
          {
            type: 'text',
            left: '65%',
            top: '60%',
            z: 100,
            style: {
              fill: '#fff',
              text: '柴油完成率',
              font: '12px Microsoft YaHei',
            }
          }],
        //backgroundColor: '#151934',
        series: [{
          type: 'liquidFill',
          radius: '50%',
          color: ['#195ba6'],
          center: ['25%', '50%'],
          data: [0.0, 0.0, 0.0, 0.0, 0.0],
          backgroundStyle: {
            borderWidth: 1,
            borderColor: '#1789fb',
            color: '#1c233f',
          },
          outline: {
            itemStyle: {
              borderWidth: 2,
              borderColor: '#1789fb',
              borderType: 'dashed',
            }
          },
          label: {
            normal: { //此处没有生效，本地生效
              textStyle: {
                fontSize: 20,
                color: '#e6e6e6',
              },
            },
          },
        },
          {
            type: 'liquidFill',
            radius: '50%',
            color: ['#195ba6'],
            center: ['70%', '50%'],
            data: [0.0, 0.0, 0.0, 0.0, 0.0],
            backgroundStyle: {
              borderWidth: 1,
              borderColor: '#1789fb',
              color: '#1c233f',
            },
            outline: {
              itemStyle: {
                borderWidth: 2,
                borderColor: '#eb5c4d',
                borderType: 'dashed',
              }
            },
            label: {
              normal: { // 同上
                textStyle: {
                  fontSize: 20,
                  color: '#e6e6e6',
                },
              },
            },

          },

        ]
      },
    }
  },
  created() {
    this.getPlanProgress();
  },
  mounted() {
    this.scrollContent();
    this.testChart();
  },
  methods: {

    testChart() {
      this.option = {
        title: {
          text: '计划完成率'
        },
        tooltip: {},
        xAxis: {
          data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
        },
        yAxis: {},
        series: [{
          name: '销量',
          type: 'bar',
          data: [5, 20, 36, 10, 10, 20]
        }]
      }
    },
    scrollContent() {
      let content = "";
      let that = this;
      that.$axios.get(that.$api.getProductList).then(function (res) {
        if (res.errcode === 0) {
          for (let i = 0; i < res.datas.length; i++) {
            let obj = res.datas[i];
            let p = "";
            if (i % 2 == 0) {
              p = "<p style='height: 40px;line-height: 40px;background-color: #BCDEFA;padding-left: 10px'>" +
                  "<b style='display: inline-block; width: 200px;'>" + obj.name + "</b>" +
                  "<b style='display: inline-block;'>" + obj.price + "<b>" +
                  "</p>";
            } else {
              p = "<p style='height: 40px;line-height: 40px ;padding-left: 10px''>" +
                  "<b style='display: inline-block; width: 200px;'>" + obj.name + "</b>" +
                  "<b style='display: inline-block;'>" + obj.price + "<b>" +
                  "</p>";
            }

            content += p;
          }

          var speed = 40
          var demo = document.getElementById("topLeft");
          var demo2 = document.getElementById("product2");
          var demo1 = document.getElementById("product1");
          demo1.innerHTML = content;
          if (demo1.clientHeight <= demo.clientHeight) {
            return;
          }

          demo2.innerHTML = demo1.innerHTML

          let Marquee = function () {
            if (demo2.offsetTop - demo.scrollTop <= 0) {

              demo.scrollTop -= demo1.offsetHeight
            } else {
              // demo2.innerHTML = demo1.innerHTML
              demo.scrollTop++
            }
          }

          var MyMar = setInterval(Marquee, speed)
          demo.onmouseover = function () {
            clearInterval(MyMar)
          }
          demo.onmouseout = function () {
            MyMar = setInterval(Marquee, speed)
          }

        } else {
          console.log(res.errmsg);
        }
      });


    },
    getPlanProgress() {

      let _this = this;
      var dateStr = formatDate(new Date(), "Y-m");
      let params = {
        monthDate: dateStr
      }
      _this.$axios.get(_this.$api.planProgress, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.buildFinishedRateChart(res.datas);
        } else {
          _this.$message.error(res.errmsg);
        }
      });

    },
    rowStyle(obj) {
      if (obj.row.name == '汽油' || obj.row.name == '柴油') {
        return {
          backgroundColor: "#cfd6d7",
          fontSize: "16px",
          fontWeight: '1000'
        }
      }

    },

    buildFinishedRateChart(data) {

      let xData = [];
      let tempData = data.filter(item => {
        if (item.name != '汽油' && item.name != '柴油') {
          return item;
        }
      })
      xData = tempData.map(item => item.name);
      let yData1 = tempData.map(item => item.plan);
      let yData2 = tempData.map(item => item.totalAmount);


      this.finishedRateOption = {
        title: {
          text: '计划完成率'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        legend: {
          data: ['计划量', '完成量']
        },
        xAxis: {
          data: xData
        },
        yAxis: {},
        series: [{
          name: '计划量',
          type: 'bar',
          data: yData1,
          color: '#38A0FF'
        },
          {
            name: '完成量',
            type: 'bar',
            data: yData2,
            color: '#91CC75'
          }]
      };

      let qyRate = data.filter(item => {
        if (item.name == '汽油') {
          return item;
        }
      });
      let cyRate = data.filter(item => {
        if (item.name == '柴油') {
          return item;
        }
      });
      let shuiwen1 = [];
      let shuiwen2 = [];
      for (let i = 0; i < 5; i++) {
        shuiwen1.push(qyRate[0].rate);
        shuiwen2.push(cyRate[0].rate);
      }
      this.totalRateOption = {
        // backgroundColor: '#151934',
        graphic: [{
          type: 'text',
          left: '20%',
          top: '60%',
          z: 100,
          style: {
            fill: '#fff',
            text: '汽油完成率',
            font: '12px Microsoft YaHei'
          },
          // children: [{
          //   type: 'text',
          //   z: 100,
          //   left: '10',
          //   top: 'middle',
          //   style: {
          //     fill: '#fff',
          //     text: '汽油',
          //     font: '12px Microsoft YaHei'
          //   }
          // }]
        },
          {
            type: 'text',
            left: '65%',
            top: '60%',
            z: 100,
            style: {
              fill: '#fff',
              text: '柴油完成率',
              font: '12px Microsoft YaHei',
            }
          }],
        //backgroundColor: '#151934',
        series: [{
          type: 'liquidFill',
          radius: '50%',
          color: ['#38A0FF'],
          center: ['25%', '50%'],
          data: shuiwen1,
          backgroundStyle: {
            borderWidth: 1,
            borderColor: '#1789fb',
            color: '#1c233f',
          },
          outline: {
            itemStyle: {
              borderWidth: 2,
              borderColor: '#1789fb',
              borderType: 'dashed',
            }
          },
          label: {
            normal: { //此处没有生效，本地生效
              textStyle: {
                fontSize: 20,
                color: '#e6e6e6',
              },
            },
          },
        },
          {
            type: 'liquidFill',
            radius: '50%',
            color: ['#38A0FF'],
            center: ['70%', '50%'],
            data: shuiwen2,
            backgroundStyle: {
              borderWidth: 1,
              borderColor: '#1789fb',
              color: '#1c233f',
            },
            outline: {
              itemStyle: {
                borderWidth: 2,
                borderColor: '#1789fb',
                borderType: 'dashed',
              }
            },
            label: {
              normal: { // 同上
                textStyle: {
                  fontSize: 20,
                  color: '#e6e6e6',
                },
              },
            },

          },

        ]
      };


    }
  },
  components: {
    'TheCharts': TheCharts
  }

}
</script>
<style scoped>
.home {
  height: 100%;
  overflow: scroll;
}

#topLeft {
  width: 100%;
  overflow: hidden;
  display: block;
  /*margin: 10px 30px;*/
  padding-left: 2px;
  text-align: left;
}

#topLeft p:nth-child(2n) {
  background: #dddddd;
}

p:nth-child(2n) {
  background: #f02d2d;
}
</style>
