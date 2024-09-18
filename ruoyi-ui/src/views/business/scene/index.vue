<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="场景名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入场景名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:scene:add']"
        >新增场景
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:scene:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sceneList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:scene:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-line-round"
            @click="beginConversation(scope.row)"
            v-hasPermi="['system:scene:edit']"
          >查看详情
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:scene:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog :title="title"
               v-if="showConversation"
               :fullscreen="true"
               :visible.sync="showConversation" width="800px"
               :center="true"
               :destroy-on-close="true">
      <conversation :sceneData="sceneData"></conversation>
    </el-dialog>
    <el-dialog :title="title"
               v-if="showEditScene"
               :visible.sync="showEditScene" width="800px"
               append-to-body
               :destroy-on-close="true">
      <edit-scene :sceneAndDialoguesData="sceneData" @closeAdd="closeAdd"></edit-scene>
    </el-dialog>
  </div>
</template>

<script>
import {delScene, getScene, listScene} from "@/api/conversation/scene";
import EditScene from "@/views/business/scene/EditScene.vue";
import Conversation from "@/views/business/scene/Conversation.vue";

export default {
  name: "Scene",
  components: {Conversation, EditScene},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 对话场景表格数据
      sceneList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
        userId: [
          {required: true, message: "$comment不能为空", trigger: "blur"}
        ],
        createTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ],
        updateTime: [
          {required: true, message: "更新时间不能为空", trigger: "blur"}
        ]
      },
      conversationTitle: '对话',
      showConversation: false,
      showEditScene: false,
      sceneUuid: '',
      sceneData: {
        name: '',
        uuid: '',
        dialogueDataList: []
      },
    };
  },
  created() {
    this.getList();
    // this.speakCommon.speechInit()
  },
  methods: {
    /** 查询对话场景列表 */
    getList() {
      this.loading = true;
      listScene(this.queryParams).then(response => {
        this.sceneList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        name: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uuid)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.showEditScene = true;
      this.title = "新增对话场景";
      this.sceneData = {
        name: '',
        uuid: '',
        introduce: '',
        studyInfo: '',
        summary: '',
        dialogueDataList: []
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.uuid || this.ids
      let params = {
        sceneUUID: id
      }
      getScene(params).then(response => {
        this.sceneData = response.data;
        this.showEditScene = true;
        this.title = "修改对话场景";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.uuid || this.ids;
      this.$modal.confirm('是否确认删除对话场景编号为"' + ids + '"的数据项？').then(function () {
        return delScene(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/scene/export', {
        ...this.queryParams
      }, `scene_${new Date().getTime()}.xlsx`)
    },
    beginConversation(row) {
      this.showConversation = true;
      this.title = "详情";
      let params = {
        sceneUUID: row.uuid
      }
      getScene(params).then(response => {
        this.sceneData = response.data;
      });
    },
    closeConversation() {
      this.showConversation = false;
    },
    speak(content) {
      this.speakCommon.speak(content)
    },
    closeAdd() {
      this.showEditScene = false;
      this.getList();
    }
  }
};
</script>
