<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入课程名称" prefix-icon="el-icon-s-grid" v-model="course"
                      class="ml-5"></el-input>
            <el-input style="width: 200px" placeholder="请输入教室" prefix-icon="el-icon-files" v-model="classroom"
                      class="ml-5"></el-input>
            <el-input style="width: 200px" placeholder="请输入教师姓名" prefix-icon="el-icon-s-custom" v-model="teacher"
                      class="ml-5"></el-input>
            <el-input style="width: 200px" placeholder="请输入提交人姓名" prefix-icon="el-icon-user-solid" v-model="informer"
                      class="ml-5"></el-input>
            <el-date-picker
                    v-model="createTime"
                    type="date"
                    placeholder="请选择信息提交日期"
                    style="width: 200px"
                    class="ml-5"
                    value-format="yyyy-MM-dd" format="yyyy-MM-dd">
            </el-date-picker>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
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
            <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="39"></el-table-column>
            <el-table-column prop="id" label="ID" width="55"></el-table-column>
            <el-table-column prop="date" label="上课日期" width="90"></el-table-column>
            <el-table-column prop="lesson" label="上课节次" width="70"></el-table-column>
            <el-table-column prop="classroom" label="教室" width="80"></el-table-column>
            <el-table-column prop="course" label="课程"></el-table-column>
            <el-table-column prop="teacher" label="教师" width="70"></el-table-column>
            <el-table-column prop="classes" label="班级" width="95"></el-table-column>
            <el-table-column prop="college" label="开课学院"></el-table-column>
            <el-table-column prop="supposedNum" label="应到人数" width="70"></el-table-column>
            <el-table-column prop="lateNum" label="迟到人数" width="70"></el-table-column>
            <el-table-column prop="exitNum" label="早退人数" width="70"></el-table-column>
            <el-table-column prop="attendNum" label="实到人数" width="70"></el-table-column>
            <el-table-column prop="informer" label="提交人" show-overflow-tooltip width="70px"></el-table-column>
            <el-table-column prop="createTime" label="提交时间" width="95px"></el-table-column>
            <el-table-column label="操作" align="center" width="100px">
                <template slot-scope="scope">
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.id)"
                    >
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
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
    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: 'Attendance',
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                dialogFormVisible: false,
                form: {},
                date: '',
                lesson: '',
                classroom: '',
                course: '',
                teacher: '',
                classes: '',
                college: '',
                supposedNum: '',
                lateNum: '',
                exitNum: '',
                attendNum: '',
                informer: '',
                createTime: '',
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
            this.load()
        },
        methods: {
            load() {
                let role = JSON.parse(localStorage.getItem("person")).role;
                if (role === "ADMIN") {
                    request.get("/attendance/page", {
                        params: {
                            pageNum: this.pageNum,
                            pageSize: this.pageSize,
                            course: this.course,
                            classroom: this.classroom,
                            teacher: this.teacher,
                            informer: this.informer,
                            createTime: this.createTime
                        }
                    }).then(res => {
                        this.tableData = res.data.records;
                        this.total = res.data.total
                    })
                } else if (role === "INFORMER") {
                    this.$router.push("/404")
                }
            },
            del(id) {
                request.delete("/attendance/" + id).then(res => {
                    if (res.code === "200") {
                        this.$message.success("删除成功");
                        this.load()
                    } else {
                        this.$message.error("删除失败")
                    }
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            delBatch() {
                let ids = this.multipleSelection.map(v => v.id);
                request.post("/attendance/del/batch", ids).then(res => {
                    if (res.code === '200') {
                        this.$message.success("批量删除成功");
                        this.load()
                    } else {
                        this.$message.error("批量删除失败")
                    }
                })
            },
            reset() {
                this.course = "";
                this.classroom = "";
                this.teacher = "";
                this.informer = "";
                this.createTime = "";
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
            exp() {
                window.open("http://localhost:9090/attendance/export");
            }
        }
    }
</script>

<style scoped>
    .headerBg {
        background: #eee !important;
    }
</style>