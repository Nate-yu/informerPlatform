<template>

    <div>
        <!--搜索-->
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

        <!--新增与导出-->
        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i>
            </el-button>
            <el-button type="warning" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
        </div>


        <!--表格数据展示-->
        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  @selection-change="handleSelectionChange">
            <el-table-column prop="id" label="ID" width="55"></el-table-column>
            <el-table-column prop="date" label="上课日期" width="90"></el-table-column>
            <el-table-column prop="lesson" label="上课节次" width="70"></el-table-column>
            <el-table-column prop="classroom" label="教室" width="80"></el-table-column>
            <el-table-column prop="course" label="课程" width="100"></el-table-column>
            <el-table-column prop="teacher" label="教师" width="70"></el-table-column>
            <el-table-column prop="classes" label="班级"></el-table-column>
            <el-table-column prop="college" label="开课学院" width="130"></el-table-column>
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

        <el-dialog title="出勤反馈" :visible.sync="dialogFormAddVisible" width="30%">
            <el-form size="small"  label-width="80px" :model="form">
                <el-form-item label="上课日期">
                    <el-date-picker
                            v-model="form.date"
                            type="date"
                            placeholder="请选择日期"
                            style="width: 100%"
                            value-format="yyyy-MM-dd" format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="课程节次" prop="lesson">
                    <el-select v-model="form.lesson" placeholder="请选择节次" style="width: 100%">
                        <el-option
                                v-for="item in lesson"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="上课教室" style="width: 100%" prop="classroom">
                    <el-input v-model="form.classroom" autocomplete="off" placeholder="如：公共楼331"></el-input>
                </el-form-item>
                <el-form-item label="课程" style="width: 100%">
                    <el-input v-model="form.course" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="教师" style="width: 100%">
                    <el-input v-model="form.teacher" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="班级" style="width: 100%">
                    <el-input v-model="form.classes" autocomplete="off" placeholder="如：计算机类[1811-1812]班"></el-input>
                </el-form-item>
                <el-form-item label="开课学院" style="width: 100%">
                    <el-input v-model="form.college" autocomplete="off" placeholder="如：计算机学院"></el-input>
                </el-form-item>
                <el-form-item label="应到人数" style="width: 100%">
                    <el-input v-model="form.supposedNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="迟到人数" style="width: 100%">
                    <el-input v-model="form.lateNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="早退人数" style="width: 100%">
                    <el-input v-model="form.exitNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="实到人数" style="width: 100%">
                    <el-input v-model="form.attendNum" autocomplete="off"></el-input>
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
        name: 'AttendanceSubmit',
        data() {
            return {
                tableData: [],
                total: 0,
                person: localStorage.getItem("person") ? JSON.parse(localStorage.getItem("person")) : {},
                pageNum: 1,
                pageSize: 5,
                dialogFormAddVisible: false,
                form: {},
                date: '',
                lesson: [{
                    value: '第1,2节',
                    label: '第1,2节'
                }, {
                    value: '第3,4节',
                    label: '第3,4节'
                }, {
                    value: '第5,6节',
                    label: '第5,6节'
                },{
                    value: '第7,8节',
                    label: '第7,8节'
                }, {
                    value: '第9,10节',
                    label: '第9,10节'
                }],
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
            this.load();
        },
        methods: {
            load() {
                let role = JSON.parse(localStorage.getItem("person")).role;
                if (role === "INFORMER") {
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
                }else if (role === "ADMIN") {
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
                this.course = "";
                this.classroom = "";
                this.teacher = "";
                this.informer = "";
                this.createTime = "";
                this.load()
            },
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                this.load()
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.load()
            },
            submit() {
                // 绑定信息员
                this.form.informer = this.person.name;
                request.post("/attendance", this.form).then(res => {
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
                request.delete("/attendance/" + id).then(res => {
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