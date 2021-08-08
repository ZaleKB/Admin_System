<template>
  <div>

    <el-form :inline="true">
      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true">Add</el-button>
      </el-form-item>
    </el-form>

    <el-table
        :data="tableData"
        style="width: 100%;margin-bottom: 20px;"
        row-key="id"
        border
        stripe
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">

      <el-table-column
          prop="name"
          label="Name"
          sortable
          width="180">
      </el-table-column>
      <el-table-column
          prop="perms"
          label="Perm Code"
          sortable
          width="180">
      </el-table-column>

      <el-table-column
          prop="icon"
          label="Icon">
      </el-table-column>

      <el-table-column
          prop="type"
          label="Type">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.type === 0">Catalog</el-tag>
          <el-tag size="small" v-else-if="scope.row.type === 1" type="success">Menu</el-tag>
          <el-tag size="small" v-else-if="scope.row.type === 2" type="info">Button</el-tag>
        </template>

      </el-table-column>

      <el-table-column
          prop="path"
          label="Menu-URL">
      </el-table-column>
      <el-table-column
          prop="component"
          label="Menu-Component">
      </el-table-column>
      <el-table-column
          prop="orderNum"
          label="OrderNum">
      </el-table-column>
      <el-table-column
          prop="statu"
          label="Status">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.statu === 1" type="success">Normal</el-tag>
          <el-tag size="small" v-else-if="scope.row.statu === 0" type="danger">Disable</el-tag>
        </template>

      </el-table-column>
      <el-table-column
          prop="icon"
          label="Operation">

        <template slot-scope="scope">
          <el-button type="text" @click="editHandle(scope.row.id)">Edit</el-button>
          <el-divider direction="vertical"></el-divider>

          <template>
            <el-popconfirm title="Are you sure to delete" @confirm="delHandle(scope.row.id)">
              <el-button type="text" slot="reference">Delete</el-button>
            </el-popconfirm>
          </template>

        </template>
      </el-table-column>

    </el-table>


    <!--Edit-->
    <el-dialog
        title="Options"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="handleClose">

      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-editForm">

        <el-form-item label="Parent" prop="parentId">
          <el-select v-model="editForm.parentId" placeholder="Please Select Parent Menu">
            <template v-for="item in tableData">
              <el-option :label="item.name" :value="item.id"></el-option>
              <template v-for="child in item.children">
                <el-option :label="child.name" :value="child.id">
                  <span>{{ "- " + child.name }}</span>
                </el-option>
              </template>
            </template>
          </el-select>
        </el-form-item>

        <el-form-item label="Name" prop="name" label-width="100px">
          <el-input v-model="editForm.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Perm-Code" prop="perms" label-width="100px">
          <el-input v-model="editForm.perms" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Icon" prop="icon" label-width="100px">
          <el-input v-model="editForm.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Menu-URL" prop="path" label-width="100px">
          <el-input v-model="editForm.path" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Component" prop="component" label-width="100px">
          <el-input v-model="editForm.component" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Type" prop="type" label-width="100px">
          <el-radio-group v-model="editForm.type">
            <el-radio :label=0>Catalog</el-radio>
            <el-radio :label=1>Menu</el-radio>
            <el-radio :label=2>Button</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="Status" prop="statu" label-width="100px">
          <el-radio-group v-model="editForm.statu">
            <el-radio :label=0>Disable</el-radio>
            <el-radio :label=1>Normal</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="OrderNum" prop="orderNum" label-width="100px">
          <el-input-number v-model="editForm.orderNum" :min="1" label="OrderNum">1</el-input-number>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" @click="submitForm('editForm')">Create</el-button>
          <el-button @click="resetForm('editForm')">Reset</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>


  </div>

</template>

<script>
export default {
  name: "Menu",
  data() {
    return {
      dialogVisible: false,
      editForm: {

      },
      editFormRules: {
        parentId: [
          {required: true, message: 'Please Select Parent', trigger: 'blur'}
        ],
        name: [
          {required: true, message: 'Please Enter Name', trigger: 'blur'}
        ],
        perms: [
          {required: true, message: 'Please Enter Code', trigger: 'blur'}
        ],
        type: [
          {required: true, message: 'Please Select Type', trigger: 'blur'}
        ],
        orderNum: [
          {required: true, message: 'Please Select Order', trigger: 'blur'}
        ],
        statu: [
          {required: true, message: 'Please Select Status', trigger: 'blur'}
        ]
      },
      tableData: []
    }
  },
  created() {
    this.getMenuTree()
  },
  methods: {
    getMenuTree() {
      this.$axios.get("/sys/menu/list").then(res => {
        this.tableData = res.data.data
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/sys/menu/' + (this.editForm.id?'update' : 'save'), this.editForm)
              .then(res => {

                this.$message({
                  showClose: true,
                  message: 'Operation Success!',
                  type: 'success',
                  onClose:() => {
                    this.getMenuTree()
                  }
                });

                this.dialogVisible = false
              })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    editHandle(id) {
      this.$axios.get('/sys/menu/info/' + id).then(res => {
        this.editForm = res.data.data

        this.dialogVisible = true
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false
      this.editForm = {}
    },
    handleClose() {
      this.resetForm('editForm')
    },
    delHandle(id) {
      this.$axios.post("/sys/menu/delete/" + id).then(res => {
        this.$message({
          showClose: true,
          message: 'Operation Success!',
          type: 'success',
          onClose:() => {
            this.getMenuTree()
          }
        });

      })
    }
  }
}
</script>

<style scoped>

</style>