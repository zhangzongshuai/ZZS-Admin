<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item>
          <el-button
            type="success"
            icon="el-icon-plus"
            @click="openAddDictDialog"
            >添加字典</el-button
          >
        </el-form-item>
        <el-form-item label="字典名称">
          <el-input
            v-model="searchKeyword"
            placeholder="请输入字典名称或编码"
            clearable
            style="width: 260px"
            @keyup.enter.native="handleSearch"
            prefix-icon="el-icon-search"
          >
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch"
            >搜索</el-button
          >
        </el-form-item>
      </el-form>

      <el-table
        :data="paginatedDictList"
        :height="tableHeight"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
        :header-cell-style="{ background: '#fafafa' }"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="80"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="dictCode"
          label="字典编码"
          min-width="150"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="dictName"
          label="字典名称"
          min-width="150"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="description"
          label="描述"
          min-width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="170"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button
                type="text"
                size="small"
                icon="el-icon-edit"
                @click="openEditDictDialog(scope.row)"
                >编辑</el-button
              >
              <el-button
                type="text"
                size="small"
                icon="el-icon-delete"
                style="color: #f56c6c"
                @click="deleteDict(scope.row)"
                >删除</el-button
              >
              <el-button
                type="text"
                size="small"
                icon="el-icon-setting"
                @click="openSubItemDialog(scope.row)"
                >配置</el-button
              >
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="filteredDictList.length"
      >
      </el-pagination>
    </el-card>

    <!-- 新增/编辑字典的对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dictDialogVisible"
      width="500px"
      :close-on-click-modal="false"
      @close="resetDictForm"
    >
      <el-form
        :model="dictForm"
        :rules="dictRules"
        ref="dictFormRef"
        label-width="100px"
      >
        <el-form-item label="字典编码" prop="dictCode">
          <el-input
            v-model="dictForm.dictCode"
            placeholder="请输入唯一编码，例如: sys_status"
            :disabled="isEditMode && !isAddSubMode"
          ></el-input>
        </el-form-item>
        <el-form-item label="字典名称" prop="dictName">
          <el-input
            v-model="dictForm.dictName"
            placeholder="请输入字典名称，例如: 系统状态"
          ></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            type="textarea"
            v-model="dictForm.description"
            rows="3"
            placeholder="可选描述信息"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dictDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDictForm">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 配置字典子项对话框 (展示子项表格，支持增删改查子项) -->
    <el-dialog
        title="配置字典子项"
        :visible.sync="subDialogVisible"
        width="800px"
        class="dict-sub-dialog"
        :close-on-click-modal="false"
        @close="closeSubDialog">
        <div style="margin-bottom: 16px; display: flex; justify-content: space-between; align-items: center;">
            <div>
                <strong style="font-size: 15px;">字典：</strong>
                <el-tag type="info" size="medium">{{ currentDict.dictName }} ( {{ currentDict.dictCode }} )</el-tag>
            </div>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="openAddSubItem">新增子项</el-button>
        </div>
        <el-table :data="currentSubItems" border stripe v-loading="subLoading" max-height="400">
            <el-table-column prop="itemId" label="子项ID" width="70" align="center"></el-table-column>
            <el-table-column prop="itemValue" label="选项值" min-width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="itemLabel" label="选项标签" min-width="140" show-overflow-tooltip></el-table-column>
            <el-table-column prop="sortOrder" label="排序" width="80" align="center"></el-table-column>
            <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip></el-table-column>
            <el-table-column label="操作" width="150" align="center" fixed="right">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="editSubItem(scope.row)">编辑</el-button>
                    <el-button type="text" size="small" style="color: #F56C6C;" @click="deleteSubItem(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div v-if="currentSubItems.length === 0" style="text-align: center; padding: 32px 0; color: #aaa;">
            暂无子项数据，点击“新增子项”进行配置
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="subDialogVisible = false">关 闭</el-button>
        </span>
    </el-dialog>

    <!-- 子项新增/编辑对话框 -->
    <el-dialog
      :title="subItemDialogTitle"
      :visible.sync="subItemFormVisible"
      width="500px"
      :close-on-click-modal="false"
      @close="resetSubItemForm"
    >
      <el-form
        :model="subItemForm"
        :rules="subItemRules"
        ref="subItemFormRef"
        label-width="100px"
      >
        <el-form-item label="选项值" prop="itemValue">
          <el-input
            v-model="subItemForm.itemValue"
            placeholder="例如: 0, 1, active 等唯一值"
          ></el-input>
        </el-form-item>
        <el-form-item label="选项标签" prop="itemLabel">
          <el-input
            v-model="subItemForm.itemLabel"
            placeholder="显示文本, 例如: 启用/停用"
          ></el-input>
        </el-form-item>
        <el-form-item label="排序号" prop="sortOrder">
          <el-input-number
            v-model="subItemForm.sortOrder"
            :min="0"
            :max="9999"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="subItemForm.remark"
            placeholder="备注信息(可选)"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="subItemFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitSubItem">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";
// 模拟后端数据存储
let dictIdCounter = 5
let subItemIdCounter = 12

// 初始化字典主表数据
const initDictData = () => [
  {
    id: 1,
    dictCode: 'sys_status',
    dictName: '系统状态',
    description: '系统通用状态(启用/停用)',
    createTime: '2024-01-10 10:00:00'
  },
  {
    id: 2,
    dictCode: 'user_type',
    dictName: '用户类型',
    description: '平台用户分类',
    createTime: '2024-02-15 14:30:00'
  },
  {
    id: 3,
    dictCode: 'gender',
    dictName: '性别',
    description: '基础性别字典',
    createTime: '2024-03-20 09:12:00'
  },
  {
    id: 4,
    dictCode: 'notice_level',
    dictName: '通知级别',
    description: '优先级等级',
    createTime: '2024-04-05 16:45:00'
  }
]

// 初始化字典子项数据 (通过字典编码关联)
const initSubItems = () => ({
  sys_status: [
    {
      itemId: 1,
      dictCode: 'sys_status',
      itemValue: '0',
      itemLabel: '停用',
      sortOrder: 1,
      remark: '禁用状态'
    },
    {
      itemId: 2,
      dictCode: 'sys_status',
      itemValue: '1',
      itemLabel: '启用',
      sortOrder: 2,
      remark: '正常状态'
    }
  ],
  user_type: [
    {
      itemId: 3,
      dictCode: 'user_type',
      itemValue: 'admin',
      itemLabel: '管理员',
      sortOrder: 1,
      remark: '超级管理员'
    },
    {
      itemId: 4,
      dictCode: 'user_type',
      itemValue: 'common',
      itemLabel: '普通用户',
      sortOrder: 2,
      remark: '一般用户'
    },
    {
      itemId: 5,
      dictCode: 'user_type',
      itemValue: 'vip',
      itemLabel: 'VIP会员',
      sortOrder: 3,
      remark: '付费会员'
    }
  ],
  gender: [
    {
      itemId: 6,
      dictCode: 'gender',
      itemValue: 'M',
      itemLabel: '男',
      sortOrder: 1,
      remark: ''
    },
    {
      itemId: 7,
      dictCode: 'gender',
      itemValue: 'F',
      itemLabel: '女',
      sortOrder: 2,
      remark: ''
    },
    {
      itemId: 8,
      dictCode: 'gender',
      itemValue: 'X',
      itemLabel: '保密',
      sortOrder: 3,
      remark: '未知'
    }
  ],
  notice_level: [
    {
      itemId: 9,
      dictCode: 'notice_level',
      itemValue: 'HIGH',
      itemLabel: '紧急',
      sortOrder: 1,
      remark: '最高优先级'
    },
    {
      itemId: 10,
      dictCode: 'notice_level',
      itemValue: 'MEDIUM',
      itemLabel: '重要',
      sortOrder: 2,
      remark: ''
    },
    {
      itemId: 11,
      dictCode: 'notice_level',
      itemValue: 'LOW',
      itemLabel: '普通',
      sortOrder: 3,
      remark: ''
    }
  ]
})

export default {
  name: 'test',
  data() {
    return {
      tableHeight: null,
      searchParams: {},
      loading: false,
      // 原始字典列表
      dictList: [],
      paginatedDictList: [],
      filteredDictList: [],
      // 搜索关键字
      searchKeyword: '',
      // 分页参数
      currentPage: 1,
      pageSize: 10,
      // 对话框
      dictDialogVisible: false,
      dialogTitle: '新增字典',
      isEditMode: false,
      isAddSubMode: false, // 标记是否在子项配置中打开父级新增(不影响)
      dictForm: {
        id: null,
        dictCode: '',
        dictName: '',
        description: ''
      },
      dictRules: {
        dictCode: [
          { required: true, message: '请输入字典编码', trigger: 'blur' },
          {
            pattern: /^[a-zA-Z0-9_]+$/,
            message: '编码只能包含字母数字下划线',
            trigger: 'blur'
          }
        ],
        dictName: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ]
      },
      // 子项相关
      subDialogVisible: false,
      currentDict: {}, // 当前选中的字典对象
      currentSubItems: [], // 当前字典的子项列表
      subLoading: false,
      // 子项表单弹窗
      subItemFormVisible: false,
      subItemDialogTitle: '新增子项',
      isEditSubMode: false,
      subItemForm: {
        itemId: null,
        dictCode: '',
        itemValue: '',
        itemLabel: '',
        sortOrder: 0,
        remark: ''
      },
      subItemRules: {
        itemValue: [
          { required: true, message: '选项值不能为空', trigger: 'blur' }
        ],
        itemLabel: [
          { required: true, message: '选项标签不能为空', trigger: 'blur' }
        ]
      },
      // 本地子项存储映射 (object: dictCode -> array)
      subItemsMap: {}
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
  },
  methods: {
    initData() {
      // 加载主字典表数据
      this.dictList = initDictData()
      // 加载子项映射
      this.subItemsMap = initSubItems()
      // 确保每个字典的子项映射都存在
      this.dictList.forEach((dict) => {
        if (!this.subItemsMap[dict.dictCode]) {
          this.subItemsMap[dict.dictCode] = []
        }
      })
    },
    // 保存子项映射到内存 (模拟持久化)
    saveSubItemsMap() {
      // 实际业务可将数据同步到localStorage或后端mock，此处直接修改内存中的subItemsMap
      // 由于已经是对引用修改，此处无需额外操作，但为了确保UI刷新，在子项保存后会重新加载currentSubItems
    },
    // 搜索处理
    handleSearch() {
      this.currentPage = 1
    },
    resetSearch() {
      this.searchKeyword = ''
      this.currentPage = 1
    },
    // 分页事件
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    // 打开新增字典对话框
    openAddDictDialog() {
      this.isEditMode = false
      this.dialogTitle = '新增字典'
      this.resetDictForm()
      this.dictDialogVisible = true
    },
    // 打开编辑字典对话框
    openEditDictDialog(row) {
      this.isEditMode = true
      this.dialogTitle = '编辑字典'
      this.dictForm = {
        id: row.id,
        dictCode: row.dictCode,
        dictName: row.dictName,
        description: row.description
      }
      this.dictDialogVisible = true
    },
    // 重置字典表单
    resetDictForm() {
      this.dictForm = {
        id: null,
        dictCode: '',
        dictName: '',
        description: ''
      }
      if (this.$refs.dictFormRef) {
        this.$refs.dictFormRef.clearValidate()
      }
    },
    // 提交字典表单
    submitDictForm() {
      this.$refs.dictFormRef.validate((valid) => {
        if (!valid) return
        if (this.isEditMode) {
          // 编辑保存
          const index = this.dictList.findIndex(
            (d) => d.id === this.dictForm.id
          )
          if (index !== -1) {
            const oldCode = this.dictList[index].dictCode
            const newCode = this.dictForm.dictCode
            // 如果修改了字典编码，需要同步子项映射的key (实际业务最好禁止修改编码或做迁移)
            if (oldCode !== newCode) {
              if (this.subItemsMap[oldCode]) {
                // 迁移子项数据
                this.subItemsMap[newCode] = this.subItemsMap[oldCode].map(
                  (item) => ({ ...item, dictCode: newCode })
                )
                delete this.subItemsMap[oldCode]
              } else if (!this.subItemsMap[newCode]) {
                this.subItemsMap[newCode] = []
              }
            }
            this.dictList[index] = { ...this.dictList[index], ...this.dictForm }
            this.$message.success('修改成功')
          }
        } else {
          // 新增
          const newId = ++dictIdCounter
          const now = new Date().toLocaleString('zh-CN')
          const newDict = {
            id: newId,
            dictCode: this.dictForm.dictCode,
            dictName: this.dictForm.dictName,
            description: this.dictForm.description,
            createTime: now
          }
          // 检查编码唯一性
          if (this.dictList.some((d) => d.dictCode === newDict.dictCode)) {
            this.$message.error('字典编码已存在，请更换')
            return
          }
          this.dictList.push(newDict)
          // 初始化子项映射
          if (!this.subItemsMap[newDict.dictCode]) {
            this.subItemsMap[newDict.dictCode] = []
          }
          this.$message.success('新增成功')
        }
        this.dictDialogVisible = false
        // 重置分页到第一页
        this.currentPage = 1
      })
    },
    // 删除字典
    deleteDict(row) {
      this.$confirm(
        `确认删除字典“${row.dictName}”吗？删除后其所有子项也将被移除。`,
        '提示',
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(() => {
          const idx = this.dictList.findIndex((d) => d.id === row.id)
          if (idx !== -1) {
            // 删除对应的子项映射
            if (this.subItemsMap[row.dictCode]) {
              delete this.subItemsMap[row.dictCode]
            }
            this.dictList.splice(idx, 1)
            this.$message.success('删除成功')
            if (this.filteredDictList.length === 0 && this.currentPage > 1) {
              this.currentPage = this.currentPage - 1
            }
          }
        })
        .catch(() => {})
    },
    // 配置子项：打开子项管理对话框
    openSubItemDialog(row) {
      this.currentDict = { ...row }
      // 加载当前字典的子项列表
      this.loadSubItems(row.dictCode)
      this.subDialogVisible = true
    },
    loadSubItems(dictCode) {
      this.subLoading = true
      // 模拟异步加载
      setTimeout(() => {
        this.currentSubItems = this.subItemsMap[dictCode]
          ? [...this.subItemsMap[dictCode]]
          : []
        // 根据sortOrder排序
        this.currentSubItems.sort(
          (a, b) => (a.sortOrder || 0) - (b.sortOrder || 0)
        )
        this.subLoading = false
      }, 100)
    },
    // 关闭子项大弹窗
    closeSubDialog() {
      this.currentDict = null
      this.currentSubItems = []
    },
    // 新增子项
    openAddSubItem() {
      if (!this.currentDict) return
      this.isEditSubMode = false
      this.subItemDialogTitle = '新增子项'
      this.resetSubItemForm()
      this.subItemForm.dictCode = this.currentDict.dictCode
      this.subItemFormVisible = true
    },
    // 编辑子项
    editSubItem(row) {
      this.isEditSubMode = true
      this.subItemDialogTitle = '编辑子项'
      this.subItemForm = {
        itemId: row.itemId,
        dictCode: row.dictCode,
        itemValue: row.itemValue,
        itemLabel: row.itemLabel,
        sortOrder: row.sortOrder,
        remark: row.remark || ''
      }
      this.subItemFormVisible = true
    },
    resetSubItemForm() {
      this.subItemForm = {
        itemId: null,
        dictCode: this.currentDict?.dictCode || '',
        itemValue: '',
        itemLabel: '',
        sortOrder: 0,
        remark: ''
      }
      if (this.$refs.subItemFormRef) this.$refs.subItemFormRef.clearValidate()
    },
    // 提交子项
    submitSubItem() {
      this.$refs.subItemFormRef.validate((valid) => {
        if (!valid) return
        const dictCode = this.subItemForm.dictCode
        if (!dictCode) return
        let targetSubList = this.subItemsMap[dictCode] || []
        if (this.isEditSubMode) {
          // 编辑
          const index = targetSubList.findIndex(
            (s) => s.itemId === this.subItemForm.itemId
          )
          if (index !== -1) {
            // 检查同一字典内itemValue唯一性（除自身外）
            const existSameValue = targetSubList.some(
              (s) =>
                s.itemValue === this.subItemForm.itemValue &&
                s.itemId !== this.subItemForm.itemId
            )
            if (existSameValue) {
              this.$message.error('选项值已存在，请确保唯一性')
              return
            }
            targetSubList[index] = {
              ...targetSubList[index],
              ...this.subItemForm
            }
            this.$message.success('修改成功')
          } else {
            this.$message.warning('子项不存在')
          }
        } else {
          // 新增: 生成新id
          const existing = targetSubList.some(
            (s) => s.itemValue === this.subItemForm.itemValue
          )
          if (existing) {
            this.$message.error('选项值重复，请重新输入')
            return
          }
          const newId = ++subItemIdCounter
          const newSub = {
            itemId: newId,
            dictCode: dictCode,
            itemValue: this.subItemForm.itemValue,
            itemLabel: this.subItemForm.itemLabel,
            sortOrder: this.subItemForm.sortOrder || 0,
            remark: this.subItemForm.remark || ''
          }
          targetSubList.push(newSub)
          this.$message.success('新增成功')
        }
        // 更新map
        this.subItemsMap[dictCode] = targetSubList
        // 刷新当前显示的子项列表
        if (this.currentDict && this.currentDict.dictCode === dictCode) {
          this.loadSubItems(dictCode)
        }
        this.subItemFormVisible = false
      })
    },
    // 删除子项
    deleteSubItem(row) {
      this.$confirm(
        `确认删除子项“${row.itemLabel}(${row.itemValue})”吗？`,
        '提示',
        {
          type: 'warning'
        }
      )
        .then(() => {
          const dictCode = row.dictCode
          let subList = this.subItemsMap[dictCode] || []
          const index = subList.findIndex((s) => s.itemId === row.itemId)
          if (index !== -1) {
            subList.splice(index, 1)
            this.subItemsMap[dictCode] = subList
            if (this.currentDict && this.currentDict.dictCode === dictCode) {
              this.loadSubItems(dictCode)
            }
            this.$message.success('删除成功')
          }
        })
        .catch(() => {})
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}
.header-toolbar {
  background: #fff;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}
.search-area {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}
.table-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.06);
  overflow-x: auto;
}
.pagination-container {
  padding: 18px 20px;
  background: #fff;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: flex-end;
}
.el-table th {
  background-color: #fafafa;
  font-weight: 600;
}
.action-buttons .el-button--small {
  margin: 0 2px;
}
.dict-sub-dialog .el-dialog__body {
  padding: 10px 20px 30px;
}
.sub-table-actions {
  margin-bottom: 15px;
  text-align: right;
}
.tag-demo {
  margin-left: 8px;
}
</style>
