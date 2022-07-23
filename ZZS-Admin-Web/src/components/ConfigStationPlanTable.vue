<template>
  <div>
    <el-table
        :data="tableData"
        :height="tableHeight"
        border
        :span-method="objectSpanMethod"
        :cell-style="{padding:'10px 0'}"
        style="width: 100%">
<!--      <el-table-column-->
<!--          type="index"-->
<!--          label="#"-->
<!--          width="80">-->
<!--      </el-table-column>-->
      <el-table-column
          prop="oilName"
          label="油品"
          width="180">
      </el-table-column>
      <el-table-column
          prop="station"
          label="到站">
      </el-table-column>
      <el-table-column
          prop="transportPlan"
          label="运输计划">
      </el-table-column>
      <el-table-column
          prop="changePlan"
          label="计划变更">
      </el-table-column>
      <el-table-column
          prop="transportType"
          label="运输方式">
        <template slot-scope="scope">
          <span>{{ scope.row.transportType === 1 ? '铁路' : '管线' }}</span>
        </template>
      </el-table-column>
      <el-table-column
          prop="changeContenArr"
          label="运输计划调整单">
        <el-table-column
            prop="changeNo"
            label="调整单号"
            width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.changeContenArr">
            <span :key="index" v-for="(item,index) in scope.row.changeContenArr">
              {{ item.changeNo }}
              <hr v-if="index != scope.row.changeContenArr.length-1" style="border:1px solid #EBEEF5">
              </span>
            </div>
          </template>
        </el-table-column>

        <el-table-column
            prop="changeDate"
            label="调整时间"
            width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.changeContenArr">
            <span :key="index" v-for="(item,index) in scope.row.changeContenArr">{{ item.changeDate }}
              <hr v-if="index != scope.row.changeContenArr.length-1" style="border:1px solid #EBEEF5">
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="changeCount"
            label="数量"
            width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.changeContenArr">
            <span :key="index" v-for="(item,index) in scope.row.changeContenArr">
              {{ item.changeCount > 0 ? '+' + item.changeCount.toString() : item.changeCount.toString() }}
              <hr v-if="index != scope.row.changeContenArr.length-1" style="border:1px solid #EBEEF5">
              </span>
            </div>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column
          prop="executePlan"
          label="执行计划">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "ConfigStationPlanTable",
  props: {
    dataParam: {
      type: Object,
      default: null
    },
    tableHeight: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      province: null,
      tableData: [],
      spanArr : [],
      pos : 0
    }
  },
  created() {
    this.getData();
  },
  methods: {
    getData() {
      let params = this.dataParam;
      this.$axios.get(this.$api.getConfigStationPlan, {params}).then(res => {
        if (res.errcode === 0) {
          this.tableData = res.data;
          this.tableData.forEach(f => {
            f.changeContenArr = JSON.parse(f.changeContent)
          })
          this.rowSpan(this.tableData)
        } else {
          this.$message.error(res.errmsg)
        }

      });
    },



    rowSpan(data) {
      this.spanArr = [];
      this.pos = 0;
      for (var i = 0; i < data.length; i++) {

        if (i === 0) {
          this.spanArr.push(1);
          this.pos = 0
        } else {
          // 判断当前元素与上一个元素是否相同
          if (data[i].oilCode === data[i - 1].oilCode) {
            this.spanArr[this.pos] += 1;
            this.spanArr.push(0);

          } else {
            this.spanArr.push(1);
            this.pos = i;
          }
        }
      }
    },
    // eslint-disable-next-line no-unused-vars
    objectSpanMethod({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    },



  },
  watch:{
    dataParam:function (){
      this.getData();
    }
  }
}
</script>

<style scoped>

</style>
