<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="serchParams" size="small">
        <el-form-item label="月份">
          <el-date-picker
              v-model="configPlanDate"
              type="month"
              value-format="yyyy-MM"
              :clearable="false"
              :picker-options="pickerOptions"
              @change="dateChange"
              placeholder="选择月">
          </el-date-picker>
        </el-form-item>
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" icon="el-icon-s-tools" @click="changePlan" :disabled="disabled">调整计划</el-button>-->
        <!--      </el-form-item>-->
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" icon="el-icon-edit" @click="modifyPlan" :disabled="disabled">编辑</el-button>-->
        <!--      </el-form-item>-->
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" @click="addStationPlan" :disabled="disabled">新增计划</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-s-tools" @click="changeStationPlan">调整计划</el-button>
        </el-form-item>
        <el-form-item>
          <el-upload
              accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
              :show-file-list="false"
              :before-upload="checkFile"
              :on-success="onUploadSuccess"
              :headers="{Authorization:auth}"
              :action="importPlanAction+configPlanDate">

            <el-button type="primary" icon="el-icon-upload">导入新计划</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-upload" @click="importChangeStationPlan">导入调整计划</el-button>
<!--          <el-upload-->
<!--              accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"-->
<!--              :show-file-list="false"-->
<!--              :before-upload="checkFile"-->
<!--              :on-success="onUploadSuccess"-->
<!--              :headers="{Authorization:auth}"-->
<!--              action="/api/stationPlan/importChangePlan">-->

<!--            <el-button type="primary" icon="el-icon-upload">导入调整计划</el-button>-->
<!--          </el-upload>-->
        </el-form-item>
      </el-form>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane :key="index" v-for="(item,index) in provinceData" :label="item.name" :name="item.name">
          <dataTable :ref="item.name" :data-param="dataParam" v-if="activeName == item.name" :table-height="tableHeight"></dataTable>
        </el-tab-pane>


        <!--      <el-tab-pane label="天津" name="天津"><dataTable :data-param="activeName" v-if="activeName == '天津'"></dataTable></el-tab-pane>-->
        <!--      <el-tab-pane label="河北" name="河北"><dataTable :data-param="activeName" v-if="activeName == '河北'"></dataTable></el-tab-pane>-->
        <!--      <el-tab-pane label="山西" name="山西"><dataTable :data-param="activeName" v-if="activeName == '山西'"></dataTable></el-tab-pane>-->
        <!--      <el-tab-pane label="内蒙古" name="内蒙古"><dataTable :data-param="activeName" v-if="activeName == '内蒙古'"></dataTable></el-tab-pane>-->
      </el-tabs>

    </el-card>
    <el-dialog
        v-dialogDrag
        title="导入调整计划"
        :visible.sync="importChangeDialog"
        :close-on-click-modal="false"
        width="60%">

      <el-form label-width="80px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="调整日期">
              <el-date-picker type="date" value-format="yyyy-MM-dd" placeholder="选择日期" v-model="importChangeDate"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="调整单号">
              <el-input v-model="importChangeNo" placeholder="请输入调整单号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-upload
          v-if="uploadShow"
          ref="changePlanUpload"
          :before-upload="checkFile"
          :on-success="onUploadSuccess"
          :headers="{Authorization:auth}"
          :action="importChangePlanAction"
          :file-list="fileList"
          :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      </el-upload>
      <span slot="footer" class="dialog-footer">
    <el-button @click="closeChangeDailog" size="small">取 消</el-button>
    <el-button type="primary" @click="submitImportStationChange" size="small">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
        v-dialogDrag
        title="新增计划"
        :visible.sync="createDialog"
        :close-on-click-modal="false"
        width="60%"
        :before-close="beforClose">
      <el-row>
        <el-col :span="8">
          <el-form label-width="80px" size="small">
            <el-form-item label="月份">
              <el-date-picker type="month" value-format="yyyy-MM" placeholder="选择日期" v-model="planDate"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="5" :offset="6">
          <span>汽油：{{ qyCount }}吨</span>
        </el-col>
        <el-col :span="5">
          <span>柴油：{{ cyCount }}吨</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-button type="primary" size="mini" @click="addStationRow">新增计划</el-button>
        </el-col>
        <el-col :span="12"></el-col>
      </el-row>
      <div style="margin: 10px 0"></div>
      <el-table :data="stationPlanData" border height="500px" style="width: 100%">
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column
            prop="oilName"
            label="油品"
            width="150">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currRow.oil" filterable placeholder="请选择油品" size="mini" value-key="code">
                <el-option
                    v-for="item in oilData"
                    :key="item.code"
                    :label="item.name"
                    :value="item">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.oilName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="station"
            label="到站"
            width="150">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currRow.station" filterable placeholder="请选择到站" size="mini" @change="stationChange">
                <el-option
                    v-for="item in stationData"
                    :key="item.id"
                    :label="item.station"
                    :value="item.station">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.station }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="province"
            label="流向"
            width="130">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currRowProvince" filterable placeholder="请选择流向" size="mini" @change="provinceChange">
                <el-option
                    v-for="item in provinceData"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.province }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="transportType"
            label="运输方式"
            width="130">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currRow.transportType" filterable placeholder="请选择运输方式" size="mini">
                <el-option
                    v-for="item in transportData"
                    :key="item.id"
                    :label="item.type"
                    :value="item.id">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.transportType === 1 ? '铁路' : '管线' }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="transportPlan"
            label="原计划"
            width="130">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-input size="mini" placeholder="请输入原计划" v-model="currRow.transportPlan">
                                </el-input>
             </span>
            <span v-else>{{ scope.row.transportPlan }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <div v-if="scope.row.isSet">
              <el-button type="primary" size="mini" @click="saveRow(scope.$index)">确定</el-button>
              <el-button size="mini" @click="cancelRow(scope.row,scope.$index)">取消</el-button>
            </div>
            <div v-else>
              <el-button size="mini" type="primary" @click="editRow(scope.row,scope.$index)">编辑</el-button>
              <el-button size="mini" type="danger" @click="deleteRow(scope.row,scope.$index)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
    <el-button @click="closeCreateDailog" size="small">取 消</el-button>
    <el-button type="primary" @click="submitStationChange" size="small">确 定</el-button>
  </span>
    </el-dialog>


    <el-dialog
        v-dialogDrag
        title="调整运输计划"
        :visible.sync="changeStationPlanDialog"
        :close-on-click-modal="false"
        width="60%"
        :before-close="beforChangeClose">
      <el-form label-width="80px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="调整日期">
              <el-date-picker type="date" value-format="yyyy-MM-dd" placeholder="选择日期" v-model="changeDate"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="调整单号">
              <el-input v-model="changeNo" placeholder="请输入调整单号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-row>
        <el-col :span="12">
          <el-button type="primary" size="mini" @click="addChangeStationRow">新增</el-button>
        </el-col>
        <el-col :span="12"></el-col>
      </el-row>
      <div style="margin: 10px 0"></div>
      <el-table :data="stationChangePlanData" border height="500px" style="width: 100%">
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column
            prop="oilName"
            label="油品"
            width="150">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currChangeRow.oil" filterable placeholder="请选择油品" size="mini" value-key="code">
                <el-option
                    v-for="item in oilData"
                    :key="item.code"
                    :label="item.name"
                    :value="item">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.oilName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="changeCount"
            label="数量"
            width="130">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-input size="mini" placeholder="请输入数量" v-model="currChangeRow.changeCount">
                                </el-input>
             </span>
            <span v-else>{{ scope.row.changeCount }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="station"
            label="到站"
            width="150">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currChangeRow.station" filterable placeholder="请选择到站" size="mini"
                         @change="stationChange2">
                <el-option
                    v-for="item in stationData"
                    :key="item.id"
                    :label="item.station"
                    :value="item.station">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.station }}</span>
          </template>
        </el-table-column>

        <el-table-column
            prop="transportType"
            label="运输方式"
            width="130">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currChangeRow.transportType" filterable placeholder="请选择运输方式" size="mini">
                <el-option
                    v-for="item in transportData"
                    :key="item.id"
                    :label="item.type"
                    :value="item.id">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.transportType === 1 ? '铁路' : '管线' }}</span>
          </template>
        </el-table-column>

        <el-table-column
            prop="province"
            label="计划属性"
            width="130">
          <template slot-scope="scope">
             <span v-if="scope.row.isSet">
              <el-select v-model="currChangeRowProvince" filterable placeholder="请选择计划属性" size="mini"
                         @change="provinceChange2">
                <el-option
                    v-for="item in provinceData"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
             </span>
            <span v-else>{{ scope.row.province }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <div v-if="scope.row.isSet">
              <el-button type="primary" size="mini" @click="saveChangeRow(scope.$index)">确定</el-button>
              <el-button size="mini" @click="cancelChangeRow(scope.row,scope.$index)">取消</el-button>
            </div>
            <div v-else>
              <el-button size="mini" type="primary" @click="editChangeRow(scope.row,scope.$index)">编辑</el-button>
              <el-button size="mini" type="danger" @click="deleteChangeRow(scope.row,scope.$index)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
    <el-button @click="closeChangeDailog" size="small">取 消</el-button>
    <el-button type="primary" @click="submitStationChange" size="small">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import {formatDate} from "@/common";
import ConfigStationPlanTable from "@/components/ConfigStationPlanTable";
import {showHeight} from "@/common/tableHeight";
let auth = window.localStorage.getItem("token");
export default {
  components: {
    "dataTable": ConfigStationPlanTable
  },
  name: "stationPlan",
  data() {
    let configDate = formatDate(new Date().getTime(), 'Y-m');
    return {
      auth: auth,
      disabled: false,
      activeName: '',
      dataParam: {},
      tableData: [],
      serchParams: {},
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      tableHeight: null,
      configPlanDate: configDate,

      //新增计划2.0
      createDialog: false,
      planDate: formatDate(new Date().getTime(), 'Y-m'),
      oilData: [],
      stationData: [],
      provinceData: [],
      transportData: [{id: 1, type: '铁路'}, {id: 2, type: '管线'}],
      stationPlanData: [],
      currRow: {},
      currRowProvince: '',
      cyCount: 0,
      qyCount: 0,

      changeDate: new Date(),
      changeNo: '',
      changeStationPlanDialog: false,
      stationChangePlanData: [],
      currChangeRow: {},
      currChangeRowProvince: '',
      loadingInstance:null,
      importPlanAction : '/api/configPlanStation/importPlan?date=',
      importChangePlanAction:'',
      importChangeDialog:false,
      fileList:[],
      importChangeDate:'',
      importChangeNo:'',
      uploadShow:true,

    }
  },
  created() {
    this.tableHeight = showHeight(300)
    window.onresize = () => {
      this.tableHeight = showHeight(300)
    }
    this.getProvinceList();
  },
  methods: {
    handleClick(tab) {
      console.log(tab)
      this.dataParam = {
        province: tab.name,
        date: this.configPlanDate
      }
    },
    dateChange() {

      this.dataParam = {
        province: this.activeName,
        date: this.configPlanDate
      }

    },

    getOilType() {
      let _this = this;
      _this.oilData = [];
      this.$axios(this.$api.getOilTypeList + '?level=3').then(function (res) {
        if (res.errcode === 0) {
          _this.oilData = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });

    },
    getStationList() {
      let _this = this;
      let params = {
        pageNum: 1,
        pageSize: 10000
      }
      _this.$axios.get(_this.$api.getStationPage, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.stationData = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    getProvinceList() {
      let params = {
        pageNum: 1,
        pageSize: 10000
      }
      this.$axios.get(this.$api.getProvinces, {params}).then(res => {
        if (res.errcode === 0) {
          this.provinceData = res.datas;
          if (res.datas.length > 0){
            this.activeName = res.datas[0].name;
            this.dataParam = {
              province: this.activeName,
              date: this.configPlanDate
            }
          }
        } else {
          this.$message.error(res.errmsg);
        }
      })
    },

    addStationPlan() {
      this.createDialog = true;
      this.getOilType();
      this.getStationList();
      this.getProvinceList();
    },
    //添加行
    addStationRow() {
      for (let i of this.stationPlanData) {
        if (i.isSet) return this.$message.warning("请先保存当前编辑项");
      }
      let j = {
        id: '',
        oilName: "",
        station: "",
        province: "",
        transportType: 1,
        transportPlan: "",
        isSet: true,
      };
      this.stationPlanData.push(j);
      this.currRow = {transportType: 1}
    },
    stationChange(val) {
      let station = this.stationData.filter(f => f.station === val)[0];
      this.currRowProvince = station.provinceName;
    },
    provinceChange(val) {
      this.currRow.province = val;
    },
    saveRow(i) {
      let temp = {};
      if (!this.stationPlanData[i].id) {
        temp.id = new Date().getTime().toString();
      } else {
        temp.id = this.stationPlanData[i].id
      }
      temp.oilCode = this.currRow.oil.code;
      temp.oilName = this.currRow.oil.name;
      temp.station = this.currRow.station;
      temp.province = this.currRow.province;
      temp.transportType = this.currRow.transportType;
      temp.transportPlan = this.currRow.transportPlan;
      temp.isSet = false;
      let newRow = JSON.parse(JSON.stringify(temp));
      this.stationPlanData.splice(i, 1, newRow)

      this.cyCount = 0
      this.qyCount = 0
      this.oilData.forEach(f => {
        if (f.baseCode === "diesel") {
          this.stationPlanData.filter(s => s.oilCode === f.code).forEach(o => this.cyCount += Number(o.transportPlan))
        } else if (f.baseCode === "gasoline") {
          this.stationPlanData.filter(s => s.oilCode === f.code).forEach(o => this.qyCount += Number(o.transportPlan))
        }

      })
    },
    cancelRow(row, i) {
      if (!row.id) {
        this.stationPlanData.splice(i, 1)
      } else {
        //this.stationPlanData[i] = JSON.parse(JSON.stringify(this.currRow));
        this.stationPlanData[i].isSet = false
      }
    },
    editRow(row, i) {
      let temp = JSON.parse(JSON.stringify(row));
      temp.id = row.id;
      temp.oil = {code: row.oilCode, name: row.oilName};
      temp.station = row.station;
      temp.province = row.province;
      temp.transportType = row.transportType;
      temp.transportPlan = row.transportPlan;
      this.currRow = JSON.parse(JSON.stringify(temp))
      this.stationPlanData[i].isSet = true;
    },
    deleteRow(row, i) {
      this.stationPlanData.splice(i, 1);
      this.cyCount = 0
      this.qyCount = 0
      this.oilData.forEach(f => {
        if (f.baseCode === "diesel") {
          this.stationPlanData.filter(s => s.oilCode === f.code).forEach(o => this.cyCount += Number(o.transportPlan))
        } else if (f.baseCode === "gasoline") {
          this.stationPlanData.filter(s => s.oilCode === f.code).forEach(o => this.qyCount += Number(o.transportPlan))
        }

      })
    },
    closeCreateDailog() {
      this.stationPlanData = [];
      this.currRow = {};
      this.qyCount = 0
      this.cyCount = 0
      this.createDialog = false;
    },
    beforClose(done) {
      this.stationPlanData = [];
      this.currRow = {}
      this.qyCount = 0
      this.cyCount = 0
      done()
    },
    submitStationPlan() {
      if (this.stationPlanData.length === 0) {
        this.$message.error("列表中没有任何记录")
        return false;
      }

      this.$confirm("请确认所输入数据的准确性后保存", "提示", {
        confirmButtonText: '已确认，现在保存',
        cancelButtonText: '再检查一下',
        type: 'warning'
      }).then(
          () => {
            let params = this.stationPlanData;
            this.$axios.post(this.$api.addStationPlan + '?type=add&date=' + this.planDate, params).then(res => {
              if (res.errcode === 0) {
                this.$message.success("操作成功")
                this.createDialog = false;
              } else {
                this.$message.error(res.errmsg)
              }
            });
          }
      ).catch(() => {
      })
    },

    changeStationPlan() {
      this.changeStationPlanDialog = true;
      this.getOilType();
      this.getStationList();
      this.getProvinceList();
    },
    importChangeStationPlan(){
      this.importChangeDialog = true;

    },
    submitUpload(){
      console.log(this.fileList)
      this.$refs.changePlanUpload.submit();
    },
    submitImportStationChange(){
     // this.importChangePlanAction = '/api/stationPlan/importChangePlan?date=' + this.importChangeDate + '&code=' + this.importChangeNo;
      this.$refs.changePlanUpload.submit();
    },
    addChangeStationRow() {
      for (let i of this.stationChangePlanData) {
        if (i.isSet) return this.$message.warning("请先保存当前编辑项");
      }
      let j = {
        id: '',
        oilName: "",
        changeCount: '',
        station: "",
        province: "",
        transportType: "",
        isSet: true,
      };
      this.stationChangePlanData.push(j);
      let obj = {
        oil: {},
        changeCount: '',
        station: '',
        transportType: 1,
        province: ''
      }
      this.currChangeRow = obj;
    },
    stationChange2(val) {
      // eslint-disable-next-line no-unused-vars
      let station = this.stationData.filter(f => f.station === val)[0];
      this.currChangeRowProvince = station.provinceName;
      this.currChangeRow.province = station.provinceName;
    },
    provinceChange2(val) {
      this.currChangeRow.province = val;
    },
    saveChangeRow(i) {
      console.log(i)
      let temp = {};
      if (!this.stationChangePlanData[i].id) {
        temp.id = new Date().getTime().toString();
      } else {
        temp.id = this.stationChangePlanData[i].id
      }
      temp.oilCode = this.currChangeRow.oil.code;
      temp.oilName = this.currChangeRow.oil.name;
      temp.station = this.currChangeRow.station;
      temp.province = this.currChangeRow.province;
      temp.transportType = this.currChangeRow.transportType;
      temp.changeCount = this.currChangeRow.changeCount;
      temp.isSet = false;
      let newRow = JSON.parse(JSON.stringify(temp));
      this.stationChangePlanData.splice(i, 1, newRow)

      // this.cyCount = 0
      // this.qyCount = 0
      // this.dataList.forEach(f => {
      //   if (f.baseCode === "diesel") {
      //     this.stationChangePlanData.filter(s => s.oilCode === f.code).forEach(o => this.cyCount += Number(o.transportPlan))
      //   } else if (f.baseCode === "gasoline") {
      //     this.stationChangePlanData.filter(s => s.oilCode === f.code).forEach(o => this.qyCount += Number(o.transportPlan))
      //   }
      //
      // })
    },
    cancelChangeRow(row, i) {
      if (!row.id) {
        this.stationChangePlanData.splice(i, 1)
      } else {
        //this.stationPlanData[i] = JSON.parse(JSON.stringify(this.currRow));
        this.stationChangePlanData[i].isSet = false
      }
    },
    editChangeRow(row, i) {
      let temp = JSON.parse(JSON.stringify(row));
      temp.id = row.id;
      temp.oil = {code: row.oilCode, name: row.oilName};
      temp.station = row.station;
      temp.province = row.province;
      temp.transportType = row.transportType;
      temp.changeCount = row.changeCount;
      this.currRow = JSON.parse(JSON.stringify(temp))
      this.stationChangePlanData[i].isSet = true;
    },
    deleteChangeRow(row, i) {
      this.stationChangePlanData.splice(i, 1);
    },
    beforChangeClose(done) {
      this.stationChangePlanData = [];
      done()
    },
    closeChangeDailog() {
      this.stationChangePlanData = [];
      this.currChangeRow = {};
      this.changeStationPlanDialog = false;
    },
    submitStationChange() {
      if (this.stationChangePlanData.length === 0) {
        this.$message.error("列表中没有任何记录")
        return false;
      }
      if (!this.changeDate) {
        this.$message.error("请选择调整时间")
        return false;
      }
      if (!this.changeNo) {
        this.$message.error("请输入调整单号")
        return false;
      }
      this.stationChangePlanData.forEach(dt => {
        dt.changeDate = this.changeDate;
        dt.changeNo = this.changeNo
      })
      this.stationChangePlanData.forEach(f => {
        if (!(f.oilCode && f.oilName && f.station && f.province && f.transportType && f.changeDate && f.changeNo)) {
          this.$message.error("请检查数据完整性")
          return false;
        }
      })

      this.$confirm("请确认所输入数据的准确性后保存", "提示", {
        confirmButtonText: '已确认，现在保存',
        cancelButtonText: '再检查一下',
        type: 'warning'
      }).then(
          () => {


            let params = this.stationChangePlanData;
            this.$axios.post(this.$api.changeStationPlan + '?date=' + this.planDate, params).then(res => {
              if (res.errcode === 0) {
                this.$message.success("操作成功")
              } else {
                this.$message.error(res.errmsg)
              }
            });
          }
      ).catch(() => {
      })
    },
//文件检查
    checkFile(file) {
      if (this.importChangeDialog){
        this.importChangeDialog = false;
      }
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
        this.$refs[this.activeName][0].getData();
      } else {
        this.$message.error("导入失败:"+ res.errmsg)
        console.log(res.errmsg);
      }

      this.loadingInstance.close();
    },
    onUploadError(err) {
      this.$message.error(err)
      this.loadingInstance.close();
    },

  },
  watch:{
    importChangeDate(val,oldVal){
      if (val && val != oldVal){
        this.uploadShow = false;
        this.importChangePlanAction = this.importChangePlanAction = '/api/configPlanStation/importChangePlan?date=' + val + '&code=' + this.importChangeNo;
        this.uploadShow = true;
      }
    },
    importChangeNo(val,oldVal){
      if (val && val != oldVal){
        this.uploadShow = false;
        this.importChangePlanAction = this.importChangePlanAction = '/api/configPlanStation/importChangePlan?date=' + this.importChangeDate + '&code=' + val;
        this.uploadShow = true;
      }
    }
  }
}
</script>

<style scoped>

</style>
