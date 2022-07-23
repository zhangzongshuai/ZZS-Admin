<template>
  <div>
    <el-row>
      <el-col span="24">
        <el-table  :data="master_user.data" border style="width: 100%" highlight-current-row>
          <el-table-column type="index"></el-table-column>
          <el-table-column :key="i" v-for="(v,i) in master_user.columns" :prop="v.field" :label="v.title"
                           :width="v.width">
            <template slot-scope="scope">
                            <span v-if="scope.row.isSet">
                                <el-input size="mini" placeholder="请输入内容" v-model="master_user.sel[v.field]">
                                </el-input>
                            </span>
              <span v-else>{{ scope.row[v.field] }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
                            <span class="el-tag el-tag--info el-tag--mini" style="cursor: pointer;"
                                  @click="pwdChange(scope.row,scope.$index,true)">
                                {{ scope.row.isSet ? '保存' : "修改" }}
                            </span>
              <span v-if="!scope.row.isSet" class="el-tag el-tag--danger el-tag--mini" style="cursor: pointer;">
                                删除
                            </span>
              <span v-else class="el-tag  el-tag--mini" style="cursor: pointer;"
                    @click="pwdChange(scope.row,scope.$index,false)">
                                取消
                            </span>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col span="24">
        <div class="el-table-add-row" style="width: 99.2%;" @click="addMasterUser()"><span>+ 添加</span></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "test",
  data() {
    return {
      master_user: {
        sel: null,//选中行
        columns: [
          {field: "type", title: "远程类型", width: 120},
          {field: "addport", title: "连接地址", width: 150},
          {field: "user", title: "登录用户", width: 120},
          {field: "pwd", title: "登录密码", width: 220},
          {field: "info", title: "其他信息"}
        ],
        data: [],
      },
    }
  },
  methods: {
    //读取表格数据
    readMasterUser() {
      //根据实际情况，自己改下啊
      this.master_user.data.map(i => {
        i.id = new Date().getTime().toString();//模拟后台插入成功后有了id
        i.isSet = false;//给后台返回数据添加`isSet`标识
        return i;
      });
    },
    //添加账号
    addMasterUser() {
      for (let i of this.master_user.data) {
        if (i.isSet) return this.$message.warning("请先保存当前编辑项");
      }
      let j = {id: 0, "type": "", "addport": "", "user": "", "pwd": "", "info": "", "isSet": true, "_temporary": true};
      this.master_user.data.push(j);
      this.master_user.sel = JSON.parse(JSON.stringify(j));
    },
    //修改
    pwdChange(row, index, cg) {
      //点击修改 判断是否已经保存所有操作
      for (let i of this.master_user.data) {
        if (i.isSet && i.id != row.id) {
          this.$message.warning("请先保存当前编辑项");
          return false;
        }
      }
      //是否是取消操作
      if (!cg) {
        console.log(111)
        if (!this.master_user.sel.id) this.master_user.data.splice(index, 1);
        return row.isSet = !row.isSet;
      }
      let that = this;
      //提交数据
      if (row.isSet) {
        //项目是模拟请求操作  自己修改下
        (function () {
          let data = JSON.parse(JSON.stringify(that.master_user.sel));
          for (let k in data) row[k] = data[k];
          that.$message({
            type: 'success',
            message: "保存成功!"
          });
          //然后这边重新读取表格数据
          that.readMasterUser();
          row.isSet = false;
        })();
      } else {
        this.master_user.sel = JSON.parse(JSON.stringify(row));
        row.isSet = true;
      }
    }
  }
}
</script>

<style scoped>

</style>
