<template>
  <div>
    <el-card class="box-card" :body-style="{padding:'20px',height:'100%'}">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item label="是否对账">

          <el-select v-model="searchParams.type" clearable placeholder="是否对账" class="mr10">
            <!--          <el-option key="0" label="全部" value="0"></el-option>-->
            <el-option key="1" label="已对账" value="1"></el-option>
            <el-option key="2" label="未对账" value="0"></el-option>
          </el-select>

        </el-form-item>
        <el-form-item label="到站">

          <station-select :current-select="''" :class-name="'mr10'" ref="station1"></station-select>

        </el-form-item>
        <el-form-item>
          <el-date-picker class="mr10" style="width:480px"
                          v-model="searchParams.time"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          unlink-panels
                          type="daterange"
                          align="right"
                          range-separator="至"
                          start-placeholder="开始日期"
                          end-placeholder="结束日期"
                          :default-time="['00:00:00', '23:59:59']"
                          :picker-options="pickerOptions1">
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-upload
              accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
              :show-file-list="false"
              :before-upload="checkFile"
              :on-success="onUploadSuccess"
              :headers="{Authorization:auth}"
              action="/api/ymdz/importddyf">
            <el-button type="primary" icon="el-icon-upload">导入</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-download" @click="download">导出</el-button>
        </el-form-item>
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" @click="checkYF"><i class="el-icon-thumb"></i>对账</el-button>-->
        <!--      </el-form-item>-->
      </el-form>

      <template>
        <div style="height: 100%;overflow: auto">
          <el-table
              ref="table"
              :data="tableData"
              border
              :height="tableHeight"
              :cell-style="{padding:'5px 0'}"
              v-loading="tableLoading"
              style="width: 100%;height: 100%">
            <el-table-column
                type="index"
                label="#"
                width="50">
            </el-table-column>
            <el-table-column
                prop="pzCode"
                label="凭证编号"
                width="180">
            </el-table-column>

            <el-table-column
                prop="remark"
                label="文本">
            </el-table-column>
            <el-table-column
                prop="ddTime"
                label="凭证日期"
                width="180"
                :formatter="timeFormater">
            </el-table-column>
            <el-table-column
                prop="freight"
                label="金额"
                width="180">
            </el-table-column>
            <el-table-column
                prop="type"
                label="状态"
                width="180">
              <template slot-scope="scope">
                <el-tag
                    effect="dark"
                    :type="scope.row.type === 0 ? 'danger' : 'success'"
                    disable-transitions>{{ scope.row.type === 0 ? '未对账' : '已对账' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
                fixed="right"
                label="操作"
                width="100">
              <template slot-scope="scope">
                <el-button v-if="scope.row.type == 0" @click="deleteDebtor(scope.row.id)" type="text" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
              class="mt10"
              background
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pageOptions.pageNum"
              :page-sizes="[20, 50, 100,200]"
              :page-size="pageOptions.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="pageOptions.total">>
          </el-pagination>

        </div>
      </template>

    </el-card>

    <!--    弹框-->
    <el-dialog
        title="选择已收回运费"
        :visible.sync="dialogVisible"
        width="70%">

      <div>
        <el-form :inline="true" :model="dialogParams" class="demo-form-inline">
          <el-form-item label="到站">
            <station-select :current-select="dialogParams.endStation" class-name="'mr10'"></station-select>

          </el-form-item>
          <el-form-item>
            <el-date-picker class="mr10" style="width:480px"
                            v-model="dialogParams.time"
                            value-format="yyyy-MM-dd"
                            type="daterange"
                            align="right"
                            unlink-panels
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :picker-options="pickerOptions2">
            </el-date-picker>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="search" @click="getDialogTable">搜索</el-button>
          </el-form-item>
        </el-form>

        <div>
          <el-table
              :data="dialogTableData"
              border
              :cell-style="{padding:'5px 0'}"
              v-loading="dialogTableLoading"
              max-height="400"
              style="width: 100%;height: 100%">
            <el-table-column
                type="index"
                label="#"
                width="50">
            </el-table-column>
            <el-table-column
                type="selection"
                width="55">
            </el-table-column>
            <el-table-column
                prop="pzCode"
                label="凭证编号"
                width="180">
            </el-table-column>
            <el-table-column
                prop="remark"
                label="文本"
                width="350">
            </el-table-column>
            <el-table-column
                prop="ddTime"
                label="凭证日期"
                width="180"
                :formatter="timeFormater">
            </el-table-column>
            <el-table-column
                prop="freight"
                label="金额"
                width="180">
            </el-table-column>
            <el-table-column
                prop="type"
                label="状态"
                width="180">
              <template slot-scope="scope">
                <el-tag
                    effect="dark"
                    :type="scope.row.type === 0 ? 'danger' : 'success'"
                    disable-transitions>{{ scope.row.type === 0 ? '未对账' : '已对账' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>


      </div>

      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
import {formatDate} from "@/common";
import StationSelect from "@/components/StationSelect";
import {showHeight} from "@/common/tableHeight";

let auth = window.localStorage.getItem("token")
export default {
  name: "ddyfqd",
  data() {
    return {
      auth: auth,
      tableHeight: null,
      tableLoading: false,
      loadingInstance: null,
      stationData: [],
      tableData: [],
      pageOptions: {
        total: 0,
        pageNum: 1,
        pageSize: 20,
        pages: 0
      },
      searchParams: {
        type: '',
        time: [],
        endStation: ''
      },
      pickerOptions1: {
        shortcuts: [
          {
            text: '本月',
            onClick(picker) {
              const now = new Date();
              const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
              const start = new Date(now.getFullYear(), now.getMonth(), 1);
              // start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '上月',
            onClick(picker) {
              const now = new Date();
              const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
              const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
              end.setDate(0);
              start.setMonth(start.getMonth() - 1);
              start.setDate(1)
              picker.$emit('pick', [start, end]);
            }
          }]
      },


      //dialog
      dialogVisible: false,
      dialogTableData: [],
      dialogTableLoading: false,
      dialogParams: {
        time: '',
        endStation: ''
      },
      pickerOptions2: {
        shortcuts: [
          {
            text: '本月',
            onClick(picker) {
              const end = new Date();
              const start = new Date(end.getFullYear(), end.getMonth(), 1);
              // start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
      }
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getJFList();
  },
  methods: {

    search() {
      this.searchParams.endStation = this.$refs.station1.selectStation;
      this.pageOptions.pageNum = 1;
      this.getJFList();
    },

    //人工对账
    handleClick(row) {
      //this.dialogVisible = true;
      //this.getDialogTable();
      console.log(row.id);
    },

    checkFile(file) {
      var ext = file.name.substring(file.name.lastIndexOf('.') + 1);
      if (ext != "xls" && ext != "xlsx") {
        this.$message.error("文件格式有误!")
        return false;
      } else {
        this.loadingInstance = this.$loading({
          text: "导入中,请稍后",
          target: document.getElementsByClassName("content-box")[0]
        });
      }

    },
    onUploadSuccess(res) {
      if (res.errcode === 0) {
        this.$message.success("导入成功")
        this.getJFList();
        this.loadingInstance.close();
      } else {
        this.$message.error("导入失败")
        this.loadingInstance.close();
      }

    },
    onUploadError(err) {
      this.$message.error("导入失败")
      console.log(err)
      this.loadingInstance.close();
    },

    getJFList() {
      console.log("jiefang")
      this.tableLoading = true;

      var params = {
        station: this.searchParams.endStation,
        type: this.searchParams.type,
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize
      }
      if (this.searchParams.time) {
        params.sTime = this.searchParams.time[0];
        params.eTime = this.searchParams.time[1];
      }
      let _this = this;
      _this.$axios.get(_this.$api.getJFPageList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
          _this.tableLoading = false;
        } else {
          _this.$message.error(res.errmsg);
          _this.tableLoading = false;
        }
      });
    },

    timeFormater(row) {
      var t = new Date(row.pzTime);
      return formatDate(t, "Y-m-d")
    },

    getDialogTable() {
      this.dialogTableLoading = true;
      var params = {
        sTime: this.dialogParams.time[0],
        eTime: this.dialogParams.time[1],
        station: this.dialogParams.endStation,
        type: 0
      }
      let _this = this;
      _this.$axios.get(_this.$api.getDfyfList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.dialogTableData = res.datas;

          _this.dialogTableLoading = false;
        } else {
          _this.$message.error(res.errmsg);
          _this.dialogTableLoading = false;
        }
      });
    },
    download() {
      var params = {
        sTime: this.searchParams.time[0],
        eTime: this.searchParams.time[1],
        station: this.$refs.station1.selectStation,
        type: this.searchParams.type
      }
      let _this = this;

      let filName = "借方导出" + formatDate(new Date(), "YmdHis") + ".xlsx";
      _this.$download(_this.$api.downloadJfExcel, filName, null, params);

    },

    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getJFList();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getJFList();

    },

    deleteDebtor(id){
      let _this = this;
      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteJf + '/' + id ).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功");
            _this.getDFPageList();
          } else {
            _this.$message.error(res.errmsg);
          }
        })
      }).catch(function () {
      });
    }
  },
  components: {
    'StationSelect': StationSelect
  }

}
</script>

<style scoped>

</style>
