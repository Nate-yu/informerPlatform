<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入标题" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-input style="width: 200px" placeholder="请输入内容关键字" suffix-icon="el-icon-key"
                      v-model="content" class="ml-5"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg">
            <el-table-column prop="id" label="ID" width="70"></el-table-column>
            <el-table-column prop="name" label="公告标题"></el-table-column>
            <el-table-column prop="createTime" label="发布时间" width="200px"></el-table-column>
            <el-table-column label="详情" align="center">
                <template slot-scope="scope">
                    <el-button type="success" @click="show(scope.row)">公告内容 <i class="el-icon-view"></i>
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

        <el-dialog title="公告信息" :visible.sync="dialogFormVisible">
            <el-form size="small">
                <el-form-item align="center">
                    <span class="title_style">{{ form.name }}</span>
                </el-form-item>
                <el-divider></el-divider>
                <el-form-item>
                    <span>{{ form.content }}</span>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: 'NoticeShow',
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                name: "",
                content: "",
                form: {},
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
                let role = JSON.parse(localStorage.getItem("person")).role;
                if (role === "INFORMER") {
                    request.get("/notice/page", {
                        params: {
                            pageNum: this.pageNum,
                            pageSize: this.pageSize,
                            name: this.name,
                            content: this.content
                        }
                    }).then(res => {
                        this.tableData = res.data.records;
                        this.total = res.data.total
                    })
                } else if (role === "ADMIN") {
                    this.$router.push("/404")
                }
            },
            reset() {
                this.name = "";
                this.content = "";
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
            show(row) {
                this.form = row;
                this.dialogFormVisible = true
            }
        }
    }
</script>

<style scoped>
    .headerBg {
        background: #eee !important;
    }

    .title_style {
        font-size: large;
        color: #409EFF;
        border-radius: 10px;
        border: solid 1px;
        padding: 5px 15px;
    }
</style>
