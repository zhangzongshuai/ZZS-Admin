<template>
  <div class="home">


    <el-row :gutter="10">
      <el-col :span="16">

        <el-card class="box-card" shadow="never">
          <el-tabs v-model="activeName">
            <el-tab-pane label="计划进度" name="plan">
              <TheCharts id="chart1" :style="{width: '100%',height:'370px'}" v-if="activeName==='plan'"
                         :option="finishedRateOption"></TheCharts>
              <el-table
                  v-if="activeName==='plan'"
                  :data="tableData"
                  :height="350"
                  style="width: 100%"
                  :cell-style="{padding:'10px 0'}"
                  :row-style="rowStyle">
                <el-table-column
                    prop="name"
                    label="油品"
                    width="120">
                </el-table-column>
                <el-table-column
                    prop="lastMonthCql"
                    label="上月超欠"
                    width="120">
                </el-table-column>
                <el-table-column
                    prop="plan"
                    label="配置计划"
                    width="120">
                </el-table-column>
                <el-table-column
                    prop="realPlan"
                    label="实际计划"
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
            </el-tab-pane>
            <el-tab-pane label="结算进度" name="settlement">
              <TheCharts id="chart2" v-if="activeName==='settlement'" :style="{width: '100%',height:'370px'}"
                         :option="settlementRateOption"></TheCharts>
              <el-table
                  v-if="activeName==='settlement'"
                  :data="settlementTable"
                  :height="350"
                  style="width: 100%"
                  :cell-style="{padding:'10px 0'}"
                  :row-style="rowStyle">
                <el-table-column
                    prop="name"
                    label="油品"
                    width="120">
                </el-table-column>
                <el-table-column
                    prop="lastMonthCql"
                    label="上月累计超欠"
                    width="120">
                </el-table-column>
                <el-table-column
                    prop="plan"
                    label="配置计划"
                    width="160">
                </el-table-column>
                <el-table-column
                    prop="realPlan"
                    label="实际计划"
                    width="160">
                </el-table-column>
                <el-table-column
                    prop="finished"
                    label="已结算量"
                    min-width="160">
                </el-table-column>
                <el-table-column
                    prop="unfinished"
                    label="结算超欠"
                    min-width="160">
                </el-table-column>
                <!--                <el-table-column-->
                <!--                    prop="unfinished"-->
                <!--                    label="未结算量"-->
                <!--                    width="150">-->
                <!--                </el-table-column>-->
                <el-table-column
                    prop="rate"
                    label="结算完成率"
                    width="120"
                >
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>

        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="box-card" shadow="never" body-style="padding:0">
          <div style="height: 200px">
            <TheCharts id="chart6" :style="{width: '100%',height:'100%'}" :option="totalRateOption"></TheCharts>
          </div>
        </el-card>
        <div style="height: 10px"></div>
        <el-card class="box-card" shadow="never" body-style="padding:0">
          <div style="height: 200px">
            <TheCharts id="chart7" :style="{width: '100%',height:'100%'}"
                       :option="settlementTotalRateOption"></TheCharts>
          </div>
        </el-card>
        <div style="height: 10px"></div>
        <el-card class="box-card" shadow="never">
          <div>
            <div
                style="background-color: #66b1ff;height: 40px;line-height: 40px;font-weight: bold;font-size: 18px;text-align: center">
              产品价格一览
            </div>
            <el-table
                :data="productTableData"
                height="310"
                style="width: 100%">
              <el-table-column
                  type="index"
                  label="#"
                  width="50">
              </el-table-column>
              <el-table-column
                  prop="name"
                  label="名称">
              </el-table-column>
              <el-table-column
                  prop="price"
                  label="价格">
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import TheCharts from '@/components/TheCharts';
import {formatDate} from "@/common";
import {accMul} from "@/common/mathUtil";

export default {
  name: "index",
  data() {
    return {
      option: {},
      tableData: [],
      finishedRateOption: {},
      totalRateOption: {},
      settlementTable: [],
      settlementRateOption: {},
      settlementTotalRateOption: {},
      activeName: 'plan',
      productTableData: []
    }
  },
  created() {
    this.getPlanProgress();
    this.getSettlementProgress();
  },
  mounted() {
    this.getProductData();
    // this.testChart();
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

    getProductData() {
      let that = this;
      that.$axios.get(that.$api.getProductList).then(res => {
        if (res.errcode === 0) {
          that.productTableData = res.datas;
          // that.scrollContent(res.datas);
        } else {
          console.log(res.errmsg);
        }
      })
    },

    scrollContent(data) {
      let content = "";
      for (let i = 0; i < data.length; i++) {
        let obj = data[i];
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
        series: [{
          type: 'liquidFill',
          radius: '70%',
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
            normal: {
              formatter: (obj) => {
                return (obj.data * 100) + '%\n\n\n' + '汽油计划完成率'
              },
              textStyle: {
                fontSize: 14,
                color: '#e6e6e6',
              },
            },
          },
        },
          {
            type: 'liquidFill',
            radius: '70%',
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
              normal: {
                formatter: (obj) => {
                  return (obj.data * 100) + '%\n\n\n柴油计划完成率';
                },
                textStyle: {
                  fontSize: 14,
                  color: '#e6e6e6',
                },
              },
            },

          },

        ]
      };


    },
    getSettlementProgress() {

      let _this = this;
      var dateStr = formatDate(new Date(), "Y-m");
      let params = {
        monthDate: dateStr
      }
      _this.$axios.get(_this.$api.settlementProgress, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.settlementTable = res.datas;
          _this.buildSettlementRateChart(res.datas);
        } else {
          _this.$message.error(res.errmsg);
        }
      });

    },
    buildSettlementRateChart(data) {
      let xData = [];
      let tempData = data.filter(item => {
        if (item.name != '汽油' && item.name != '柴油') {
          return item;
        }
      })
      xData = tempData.map(item => item.name);
      let yData1 = tempData.map(item => item.plan);
      let yData2 = tempData.map(item => item.finished);


      this.settlementRateOption = {
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
      this.settlementTotalRateOption = {
        series: [{
          type: 'liquidFill',
          radius: '70%',
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
            normal: {
              formatter: (obj) => {
                return accMul(obj.data , 100) + '%\n\n\n' + '汽油结算完成率'
              },
              textStyle: {
                fontSize: 14,
                color: '#e6e6e6',
              },
            },
          },
        },
          {
            type: 'liquidFill',
            radius: '70%',
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
              normal: {
                formatter: (obj) => {
                  return (obj.data * 100) + '%\n\n柴油结算完成率';
                },
                textStyle: {
                  fontSize: 14,
                  color: '#e6e6e6',
                },
              },
            },

          },

        ]
      };


    },
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
