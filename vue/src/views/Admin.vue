<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i>
            </el-button>
            <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定批量删除这些数据吗？"
                    @confirm="delBatch"
            >
                <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i>
                </el-button>
            </el-popconfirm>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" ></el-table-column>
            <el-table-column prop="id" label="ID" ></el-table-column>
            <el-table-column prop="name" label="用户名" ></el-table-column>
            <el-table-column label="操作"  align="center">
                <template slot-scope="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i>
                    </el-button>
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.id)"
                    >
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i>
                        </el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" size="small" :rules="rules" :model="form" ref="form">
                <el-form-item label="用户名" prop="name">
                    <el-input v-model="form.name" autocomplete="off" ></el-input>
                </el-form-item>
                <el-form-item label="密码" style="width: 100%" prop="password">
                    <el-input v-model="form.password" autocomplete="off" placeholder="默认密码admin, 为空则不修改"></el-input>
                </el-form-item>
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: 'Admin',
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                form: {},
                name: '',
                password: '',
                dialogFormVisible: false,
                multipleSelection: [],
                collapseBtnClass: 'el-icon-s-fold',
                isCollapse: false,
                sideWidth: 200,
                logoTextShow: true,
                headerBg: 'headerBg',
                rules: {
                    name: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
                    ],
                    password: []
                }
            }
        },
        created() {
            // 请求分页查询数据
            this.load()
        },
        methods: {
            load() {
                let role = JSON.parse(localStorage.getItem("person")).role;
                if (role === "ADMIN") {
                    request.get("/admin/page", {
                        params: {
                            pageNum: this.pageNum,
                            pageSize: this.pageSize,
                            name: this.name
                        }
                    }).then(res => {
                        this.tableData = res.data.records;
                        this.total = res.data.total
                    })
                } else if (role === "INFORMER") {
                    this.$router.push("/404")
                }
            },
            save() {
                this.$refs['form'].validate((valid) => {
                    if (valid) {  // 表单校验合法
                        request.post("/admin", this.form).then(res => {
                            if (res) {
                                this.$message.success("保存成功");
                                this.dialogFormVisible = false;
                                this.load()
                            } else {
                                this.$message.error("保存失败")
                            }
                        })
                    } else {
                        return false;
                    }
                });
            },
            handleAdd() {
                this.dialogFormVisible = true;
                this.form = {}
            },
            handleEdit(row) {
                this.form = row;
                this.dialogFormVisible = true
            },
            del(id) {
                request.delete("/admin/" + id).then(res => {
                    if (res.code === '200') {
                        this.$message.success("删除成功");
                        this.load()
                    } else {
                        this.$message.error("删除失败")
                    }
                })
            },
            handleSelectionChange(val) {
                console.log(val);
                this.multipleSelection = val
            },
            delBatch() {
                let ids = this.multipleSelection.map(v => v.id);
                request.post("/admin/del/batch", ids).then(res => {
                    if (res.code === '200') {
                        this.$message.success("批量删除成功");
                        this.load()
                    } else {
                        this.$message.error("批量删除失败")
                    }
                })
            },
            reset() {
                this.name = "";
                this.load()
            },
            handleSizeChange(pageSize) {
                console.log(pageSize);
                this.pageSize = pageSize;
                this.load()
            },
            handleCurrentChange(pageNum) {
                console.log(pageNum);
                this.pageNum = pageNum;
                this.load()
            }
        }
    }
</script>

<style scoped>
    .headerBg {
        background: #eee !important;
    }
</style>