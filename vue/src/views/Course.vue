<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入课程名称" prefix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-select v-model="lesson" placeholder="请选择课程节次" style="width: 200px" prefix-icon="el-icon-timer"
                       class="ml-5">
                <el-option
                        v-for="item in lessonOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <el-select v-model="weekDay" placeholder="请选择上课日期" style="width: 200px" prefix-icon="el-icon-time"
                       class="ml-5">
                <el-option
                        v-for="item in weekDayOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <el-input style="width: 200px" placeholder="请输入上课教室" prefix-icon="el-icon-location-outline"
                      v-model="classroom" class="ml-5"></el-input>
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
            <el-upload action="http://localhost:9090/course/import" :show-file-list="false" accept="xlsx"
                       :on-success="handleExcelImportSuccess" style="display: inline-block">
                <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
            </el-upload>
            <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="70"></el-table-column>
            <el-table-column prop="weekDay" label="上课日期" width="100"></el-table-column>
            <el-table-column prop="lesson" label="课程节次" width="80"></el-table-column>
            <el-table-column prop="name" label="课程名称" width="100"></el-table-column>
            <el-table-column prop="teacherName" label="教师姓名" width="100"></el-table-column>
            <el-table-column prop="teacherTitle" label="教师职称" width="80"></el-table-column>
            <el-table-column prop="classroom" label="上课教室" width="100"></el-table-column>
            <el-table-column prop="numbers" label="应到人数" width="50"></el-table-column>
            <el-table-column prop="classes" label="上课班级" width="200"></el-table-column>
            <el-table-column prop="college" label="开课学院" width="120"></el-table-column>
            <el-table-column label="操作" align="center">
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

        <el-dialog title="课程信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" size="small">
                <el-form-item label="课程日期">
                    <el-select v-model="form.weekDay" placeholder="请选择日期" style="width: 326px">
                        <el-option
                                v-for="item in weekDayOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="课程节次">
                    <el-select v-model="form.lesson" placeholder="请选择节次" style="width: 326px">
                        <el-option
                                v-for="item in lessonOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="课程名称">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="教师姓名">
                    <el-input v-model="form.teacherName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="教师职称">
                    <el-input v-model="form.teacherTitle" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="上课教室">
                    <el-input v-model="form.classroom" autocomplete="off" placeholder="如：公共楼331"></el-input>
                </el-form-item>
                <el-form-item label="应到人数">
                    <el-input v-model="form.numbers" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="上课班级">
                    <el-input v-model="form.classes" autocomplete="off" placeholder="如：计算机1802班"></el-input>
                </el-form-item>
                <el-form-item label="开课学院">
                    <el-input v-model="form.college" autocomplete="off" placeholder="如：计算机学院"></el-input>
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
        name: 'Course',
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                name: "",
                lesson: "",
                weekDay: "",
                classroom: "",
                teacherName: "",
                teacherTitle: "",
                numbers: "",
                college: "",
                classes: "",
                form: {},
                weekDayOptions: [{
                    value: '星期一',
                    label: '星期一'
                }, {
                    value: '星期二',
                    label: '星期二'
                }, {
                    value: '星期三',
                    label: '星期三'
                }, {
                    value: '星期四',
                    label: '星期四'
                }, {
                    value: '星期五',
                    label: '星期五'
                }, {
                    value: '星期六',
                    label: '星期六'
                }],
                lessonOptions: [{
                    value: '第1,2节',
                    label: '第1,2节'
                }, {
                    value: '第3,4节',
                    label: '第3,4节'
                }, {
                    value: '第5,6节',
                    label: '第5,6节'
                }, {
                    value: '第7,8节',
                    label: '第7,8节'
                }, {
                    value: '第9,10节',
                    label: '第9,10节'
                }],
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
            this.load()
        },
        methods: {
            load() {
                request.get("/course/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        name: this.name,
                        lesson: this.lesson,
                        weekDay: this.weekDay,
                        classroom: this.classroom
                    }
                }).then(res => {
                    this.tableData = res.data.records;
                    this.total = res.data.total
                })
            },
            save() {
                let role = JSON.parse(localStorage.getItem("person")).role;
                if (role === "ADMIN") {
                    request.post("/course", this.form).then(res => {
                        if (res) {
                            this.$message.success("保存成功");
                            this.dialogFormVisible = false;
                            this.load()
                        } else {
                            this.$message.error("保存失败")
                        }
                    })
                } else if (role === "INFORMER") {
                    this.$router.push("/404")
                }
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
                request.delete("/course/" + id).then(res => {
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
                request.post("/course/del/batch", ids).then(res => {
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
                this.lesson = "";
                this.weekDay = "";
                this.classroom = "";
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
                window.open("http://localhost:9090/course/export");
            }
        }
    }
</script>

<style scoped>
    .headerBg {
        background: #eee !important;
    }
</style>