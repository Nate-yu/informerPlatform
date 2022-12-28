<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-input style="width: 200px" placeholder="请输入学号" suffix-icon="el-icon-user-solid"
                      v-model="stuNum" class="ml-5"></el-input>
            <el-input style="width: 200px" placeholder="请输入电话" suffix-icon="el-icon-phone"
                      v-model="phone" class="ml-5"></el-input>
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
            <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx"
                       :on-success="handleExcelImportSuccess" style="display: inline-block">
                <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
            </el-upload>
            <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="70"></el-table-column>
            <el-table-column prop="stuNum" label="学号" width="100"></el-table-column>
            <el-table-column prop="name" label="姓名" width="80"></el-table-column>
            <el-table-column prop="sex" label="性别" width="50"></el-table-column>
            <el-table-column prop="college" label="学院" width="100"></el-table-column>
            <el-table-column prop="major" label="专业" width="120"></el-table-column>
            <el-table-column prop="classes" label="班级" width="50"></el-table-column>
            <el-table-column prop="proof" label="学历" width="60"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="170"></el-table-column>
            <el-table-column prop="phone" label="电话" width="100"></el-table-column>
            <el-table-column prop="qq" label="QQ号" width="100"></el-table-column>
            <el-table-column label="操作" width="191" align="center">
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
                <el-form-item label="姓名" style="width: 100%" prop="name">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="sex">
                    <el-select v-model="form.sex" placeholder="请选择性别" style="width: 100%">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="学号" style="width: 100%" prop="stuNum">
                    <el-input v-model="form.stuNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" style="width: 100%" prop="password">
                    <el-input v-model="form.password" autocomplete="off" placeholder="默认密码123456, 为空则不修改"></el-input>
                </el-form-item>
                <el-form-item label="学院" style="width: 100%" prop="college">
                    <el-input v-model="form.college" autocomplete="off" placeholder="如：计算机学院"></el-input>
                </el-form-item>
                <el-form-item label="专业" style="width: 100%" prop="major">
                    <el-input v-model="form.major" autocomplete="off" placeholder="如：计算机科学与技术"></el-input>
                </el-form-item>
                <el-form-item label="班级" style="width: 100%" prop="classes">
                    <el-input v-model="form.classes" autocomplete="off" placeholder="请填写班级数字"></el-input>
                </el-form-item>
                <el-form-item label="学历" style="width: 100%" prop="proof">
                    <el-select v-model="form.proof" placeholder="请选择学历" style="width: 100%">
                        <el-option
                                v-for="item in options1"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="电话" style="width: 100%" prop="phone">
                    <el-input v-model="form.phone" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" style="width: 100%">
                    <el-input v-model="form.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="QQ号" style="width: 100%">
                    <el-input v-model="form.qq" autocomplete="off"></el-input>
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
        name: 'User',
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                form: {},
                name: '',
                stuNum: '',
                password: '',
                sex: '',
                major: '',
                classes: '',
                phone: '',
                email: '',
                proof: '',
                qq: '',
                options: [{
                    value: '男',
                    label: '男'
                }, {
                    value: '女',
                    label: '女'
                }],
                options1: [{
                    value: '本科',
                    label: '本科'
                }, {
                    value: '专科',
                    label: '专科'
                }, {
                    value: '研究生',
                    label: '研究生'
                }],
                rules: {
                    name: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    stuNum: [
                        {required: true, message: '请输入学号', trigger: 'blur'},
                        {min: 11, max: 11, message: '长度在 11 个数字', trigger: 'blur'}
                    ],
                    sex: [
                        { required: true, message: '请选择性别', trigger: 'change' }
                    ],
                    phone: [
                        {required: true, message: '请输入电话', trigger: 'blur'},
                        {min: 1, max: 11, message: '长度在 1 到 11 个数字', trigger: 'blur'}
                    ],
                    college: [
                        {required: true, message: '请输入学院', trigger: 'blur'}
                    ],
                    major: [
                        {required: true, message: '请输入专业', trigger: 'blur'}
                    ],
                    classes: [
                        {required: true, message: '请输入班级', trigger: 'blur'}
                    ],
                    proof: [
                        {required: true, message: '请选择学历', trigger: 'change'}
                    ],
                    password: []
                },
                dialogFormVisible: false,
                multipleSelection: [],
                collapseBtnClass: 'el-icon-s-fold',
                isCollapse: false,
                sideWidth: 200,
                logoTextShow: true,
                headerBg: 'headerBg'
            }
        },
        created() {
            // 请求分页查询数据
            this.load();
        },
        methods: {
            load() {
                let role = JSON.parse(localStorage.getItem("person")).role;
                if (role === "ADMIN") {
                    request.get("/user/page", {
                        params: {
                            pageNum: this.pageNum,
                            pageSize: this.pageSize,
                            name: this.name,
                            stuNum: this.stuNum,
                            phone: this.phone
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
                        request.post("/user", this.form).then(res => {
                            if (res.code === "200") {
                                this.$message.success("保存成功");
                                this.dialogFormVisible = false;
                                this.load()
                            } else {
                                this.$message.error(res.msg)
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
                request.delete("/user/" + id).then(res => {
                    if (res.code==="200") {
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
                request.post("/user/del/batch", ids).then(res => {
                    if (res.data) {
                        this.$message.success("批量删除成功");
                        this.load()
                    } else {
                        this.$message.error("批量删除失败")
                    }
                })
            },
            reset() {
                this.name = "";
                this.stuNum = "";
                this.phone = "";
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
            },
            handleExcelImportSuccess() {
                this.$message.success("导入成功");
                this.load();
            },
            exp() {
                window.open("http://localhost:9090/user/export");
            }
        }
    }
</script>

<style scoped>
    .headerBg {
        background: #eee !important;
    }
</style>