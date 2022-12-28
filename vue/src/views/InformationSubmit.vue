<template>

    <div>
        <!--搜索-->
        <div style="margin: 10px 0">
            <el-select v-model="type" placeholder="请选择反馈类型" style="width: 200px" class="ml-5">
                <el-option
                        v-for="item in typesFind"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <el-input style="width: 200px" placeholder="请输入信息员" prefix-icon="el-icon-user-solid" v-model="informer"
                      class="ml-5"></el-input>
            <el-date-picker
                    v-model="time"
                    type="date"
                    placeholder="请选择日期"
                    style="width: 200px"
                    class="ml-5"
                    value-format="yyyy-MM-dd" format="yyyy-MM-dd">
            </el-date-picker>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <!--新增与导出-->
        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i>
            </el-button>
            <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
        </div>


        <!--表格数据展示-->
        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  @selection-change="handleSelectionChange">
            <el-table-column prop="id" label="ID" width="70"></el-table-column>
            <el-table-column prop="informer" label="信息员" width="100" show-overflow-tooltip></el-table-column>
            <el-table-column prop="time" label="信息提交时间" width="200"></el-table-column>
            <el-table-column prop="type" label="信息反馈类型" width="150"></el-table-column>
            <el-table-column prop="content" label="信息反馈主要内容" show-overflow-tooltip></el-table-column>
            <el-table-column prop="state" label="处理状态" width="100">
                <template slot-scope="scope">
                    <div :style="{ color: scope.row.state === '已处理' ? 'green' : 'red'}">
                        {{ scope.row.state }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="300">
                <template slot-scope="scope">
                    <el-button type="success" @click="checkAndHandle(scope.row)">查看 <i class="el-icon-view"></i>
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
                        <el-button type="danger" slot="reference" v-if="scope.row.informer === person.name">删除 <i class="el-icon-remove-outline"></i></el-button>
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

        <el-dialog title="处理反馈" :visible.sync="dialogFormVisible" width="50%">
            <el-form size="small">
                <el-form-item>
                    <span> <b>信息员: </b> {{ form.informer }}</span>
                </el-form-item>
                <el-divider></el-divider>
                <el-form-item>
                    <span> <b>信息提交时间: </b>{{ form.time }}</span>
                </el-form-item>
                <el-divider></el-divider>
                <el-form-item>
                    <span> <b>反馈类型: </b>{{ form.type }}</span>
                </el-form-item>
                <el-divider></el-divider>
                <el-form-item>
                    <span> <b>主要内容及建议: </b>{{ form.content }}</span>
                </el-form-item>
                <el-divider></el-divider>
                <el-form-item>
                    <b> 信息处理与回复: </b>
                    <el-input
                            type="textarea"
                            :rows="3"
                            disabled
                            v-model="form.handleReply">
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="信息反馈" :visible.sync="dialogFormAddVisible" width="50%">
            <el-form size="small">
                <el-form-item label="信息反馈类型" style="width: 100%" prop="type">
                    <el-select v-model="form.type" placeholder="请选择反馈类型" style="width: 100%">
                        <el-option
                                v-for="item in types"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="主要内容及建议" style="width: 100%" prop="content">
                    <el-input
                            type="textarea"
                            :rows="3"
                            placeholder="请输入内容"
                            v-model="form.content">
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormAddVisible = false">取 消</el-button>
                <el-button type="primary" @click="submit">提 交</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: 'Information',
        data() {
            return {
                tableData: [],
                total: 0,
                person: localStorage.getItem("person") ? JSON.parse(localStorage.getItem("person")) : {},
                pageNum: 1,
                pageSize: 5,
                dialogFormVisible: false,
                dialogFormAddVisible: false,
                form: {},
                informer: '',
                type: '',
                content: '',
                state: '',
                time: '',
                handleReply: "",
                types: [{
                    value: '教师教学方面',
                    label: '教师教学方面'
                }, {
                    value: '教学管理方面',
                    label: '教学管理方面'
                }, {
                    value: '学生学习状况',
                    label: '学生学习状况'
                }, {
                    value: '教学条件',
                    label: '教学条件'
                }, {
                    value: '其他方面',
                    label: '其他方面'
                }],
                typesFind: [{
                    value: '',
                    label: '所有'
                }, {
                    value: '教师教学方面',
                    label: '教师教学方面'
                }, {
                    value: '教学管理方面',
                    label: '教学管理方面'
                }, {
                    value: '学生学习状况',
                    label: '学生学习状况'
                }, {
                    value: '教学条件',
                    label: '教学条件'
                }, {
                    value: '其他方面',
                    label: '其他方面'
                }],
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
                if (role === "INFORMER") {
                    request.get("/information/page", {
                        params: {
                            pageNum: this.pageNum,
                            pageSize: this.pageSize,
                            type: this.type,
                            informer: this.informer,
                            time: this.time
                        }
                    }).then(res => {
                        this.tableData = res.data.records;
                        this.total = res.data.total
                    })
                } else if (role === "ADMIN") {
                    this.$router.push("/404")
                }

            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handleAdd() {
                this.dialogFormAddVisible = true;
                this.form = {}
            },
            reset() {
                this.type = "";
                this.informer = "";
                this.time = "";
                this.load()
            },
            handleSizeChange(pageSize) {
                console.log(pageSize);
                this.pageSize = pageSize;
                this.load()
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.load()
            },
            checkAndHandle(row) {
                this.form = row;
                this.dialogFormVisible = true;
            },
            submit() {
                this.form.state = "未处理";
                // 绑定信息员
                this.form.informer = this.person.name;
                request.post("/information", this.form).then(res => {
                    if (res.code === '200') {
                        this.$message.success("保存成功");
                        this.dialogFormAddVisible = false;
                        this.load()
                    } else {
                        this.$message.error("保存失败")
                    }
                })
            },
            del(id) {
                request.delete("/information/" + id).then(res => {
                    if (res.code==="200") {
                        this.$message.success("删除成功");
                        this.load()
                    } else {
                        this.$message.error("删除失败")
                    }
                })
            },
            exp() {
                window.open("http://localhost:9090/information/export");
            }
        }
    }
</script>

<style scoped>
    .headerBg {
        background: #eee !important;
    }
</style>