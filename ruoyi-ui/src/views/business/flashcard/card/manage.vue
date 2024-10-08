<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleUpdate"
        >修改
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
        >删除
        </el-button>
        <el-button
          type="warning"
          plain
          icon="el-icon-folder"
          size="mini"
          :disabled="multiple"
          @click="addCardsToPackage"
        >关联到卡包
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="正面" align="center" prop="front"/>
      <el-table-column label="反面" align="center" prop="back"/>
      <el-table-column label="类型" align="center" :formatter="formatType" prop="type"/>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="showPackages(scope.row)"
          >查看关联卡包
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

    <!-- 添加或修改用户单词对话框 -->
    <el-dialog v-if="open" :title="title" :visible.sync="open"
               append-to-body destroy-on-close width="800px" @before-close="cancel">
      <EditCustomizeCard @closeDialog="closeEditCustomizeCard"></EditCustomizeCard>
    </el-dialog >
    <el-dialog v-if="showEditor" :visible.sync="showEditor" append-to-body destroy-on-close
               title="修改卡片" width="800px">
      <EditCard :card-uuid="this.cardUUID" @closeEditCard="cancelEdit" @refreshList="getList"></EditCard>
    </el-dialog>
    <el-dialog title="查看卡包"
               v-if="showPackageView"
               :visible.sync="showPackageView"
               :center="true"
               :destroy-on-close="true">
      <PackageView :card-u-u-id="cardUUID"></PackageView>
    </el-dialog>
    <el-dialog title="选择卡包" :visible.sync="showPackageSelect" width="500px" append-to-body destroy-on-close>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="选择卡包">
          <el-select filterable v-model="form.packageUUID" placeholder="请选择卡包">
            <el-option v-for="item in packageOptions"
                       :key="item.value"
                       :label="item.name"
                       :value="item.uuid">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCardsToPackage">确 定</el-button>
        <el-button @click="cancelCardsToPackage">取 消</el-button>
      </div>
    </el-dialog >
  </div>
</template>

<script>
import LabelTag from "@/components/Tag/index.vue";
import {addCardsToPackage, del, listCards} from "@/api/bussiness/flashcard";
import {searchWord} from "@/api/bussiness/word";
import {getPackageList} from "@/api/bussiness/flashcardpackage";
import WordView from "@/views/business/language/wordcollection/wordView.vue";
import PackageView from "@/views/business/flashcard/card/PackageView.vue";
import EditCustomizeCard from "@/views/business/flashcard/card/EditCustomizeCard.vue";
import EditCard from "@/views/business/flashcard/card/EditCard.vue";

export default {
  name: "card_manage",
  components: {
    EditCard,
    EditCustomizeCard,
    PackageView,
    WordView,
    LabelTag,
  },
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
      // 用户单词表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null
      },
      // 表单参数
      form: {
        type: null,
        wordUUID: "",
        questionUUID: "",
        packageUUID: ""
      },
      typeOptions: [{
        value: 1,
        label: '单词卡片',
      }, {
        value: 2,
        label: '问题卡片',
      }],
      wordOptions: [],
      questionOptions: [],
      packageOptions: [],
      labelType: 3,
      showSelect: true,
      // 表单校验
      rules: {
        wordUUID: [
          {required: true, message: "单词不能为空", trigger: "blur"}
        ],
        packageUUID: [
          {required: true, message: "卡包不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "卡片类型不能为空", trigger: "blur"}
        ]
      },
      showPackageView: false,
      cardUUID: "",
      showSelectWord: false,
      showSelectQuestion: false,
      showEditor: false,
      showPackageSelect: false
    };
  },
  created() {
    this.getList();
    getPackageList().then(res => {
      this.packageOptions = res.data;
      console.log(this.options)
    })
  },
  methods: {
    /** 查询用户单词列表 */
    getList() {
      this.loading = true;
      listCards(this.queryParams).then(response => {
        this.dataList = response.rows;
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
        packageUUID : null,
        name: null,
        type: null,
        labelInfos: [],
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
      this.reset();
      this.open = true;
      this.title = "添加卡片";
    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.reset();
      const id = this.ids
      if (!id || id.length === 0 || id.length > 1) {
        this.$modal.msgError("请选择一条记录");
        return
      }
      this.cardUUID = id[0];
      this.showEditor = true;
      this.title = "修改卡片";
    },
    cancelEdit() {
      this.showEditor = false
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function () {
        return del(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    queryWord(query) {
      if (query !== '') {
        this.loading = true;
        let params = {
          word: query
        }
        searchWord(params).then(res => {
          this.wordOptions = []
          this.wordOptions = res.data
          this.loading = false;
        })
      } else {
        this.options = [];
      }
    },
    searchQuestion(query) {
      if (query !== '') {
        this.loading = true;
        let params = {
          word: query
        }
        // searchQuestion(params).then(res => {
        //   this.questionOptions = []
        //   this.questionOptions = res.data
        //   this.loading = false;
        // })
      } else {
        this.options = [];
      }
    },
    formatType(row, column) {
      // 获取当前行的属性值
      const type = row[column.property];
      if (type  === 1) {
        return "单词卡片"
      } else if (type === 2) {
        return "问题卡片"
      } else if (type === 3) {
        return "表达卡片"
      } else if (type === 4) {
        return "自定义卡片"
      }
    },
    showPackages(row) {
      this.showPackageView = true
      this.cardUUID = row.uuid
    },
    cardTypeChange(type) {
      this.showSelectWord = false
      this.showSelectQuestion = false
      if (type === 1) {
        this.showSelectWord = true
      } else if (type === 2) {
        this.showSelectQuestion = true
      }
    },
    addCardsToPackage() {
      this.showPackageSelect = true
    },
    submitCardsToPackage() {
      let data = {
        packageUUId: this.form.packageUUID,
        cardUUIdList: this.ids
      }
      addCardsToPackage(data).then(response => {
        if (response.data.code === 200) {
          if(response.data.msg){
            this.$modal.msgSuccess(response.data.msg);
          } else {
            this.$modal.msgSuccess("添加成功");
          }
          this.showPackageSelect = false;
          this.getList();
        } else {
          this.$modal.msgError("添加失败");
        }
      });
    },
    cancelCardsToPackage() {
      this.showPackageSelect = false
      this.reset()
    },
    closeEditCustomizeCard() {
      this.open = false
      this.getList()
    }
  }
};
</script>
<style>
</style>
