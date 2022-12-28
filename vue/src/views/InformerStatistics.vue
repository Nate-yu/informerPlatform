<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入信息员姓名" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg">
            <el-table-column prop="id" label="ID" width="70"></el-table-column>
            <el-table-column prop="name" label="信息员"></el-table-column>
            <el-table-column prop="attSubmit" label="出勤反馈次数"></el-table-column>
            <el-table-column prop="infoSubmit" label="信息反馈次数"></el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button type="success" @click="view(scope.row.id)">查看个人信息 <i class="el-icon-edit"></i>
                    </el-button>
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
        name: 'InformerStatistics',
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                student: {},
                form: {},
                name: '',
                attSubmit: '',
                InfoSubmit: '',
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
                    request.get("/user/statistics", {
                        params: {
                            pageNum: this.pageNum,
                            pageSize: this.pageSize,
                            name: this.name,
                        }
                    }).then(res => {
                        this.tableData = res.data.records;
                        this.total = res.data.total
                    })
                } else if (role === "INFORMER") {
                    this.$router.push("/404")
                }
            },
            reset() {
                this.name = "";
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
            view(id) {
                request.get("/user/" + id).then(res => {
                    localStorage.setItem("student",JSON.stringify(res.data));
                    this.$router.push("/studentInformation")
                })
            }
        }
    }
</script>

<style scoped>
    .headerBg {
        background: #eee !important;
    }
</style>