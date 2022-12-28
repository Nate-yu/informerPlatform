<template>
    <el-card style="width: 500px;">
        <el-form label-width="80px" size="small">
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
            <el-form-item>
                <el-button type="primary" @click="save">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
    import request from "../utils/request";
    export default {
        name: "PersonInformation",
        data() {
            return {
                form: {},
                person: localStorage.getItem("person") ? JSON.parse(localStorage.getItem("person")) : {},
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
            }
        },
        created() {
            this.getUser().then(res => {
                this.form = res
            })
        },
        methods: {
            async getUser() {
                return (await request.get("/user/name/" + this.person.name)).data
            },
            save() {
                request.post("/user", this.form).then(res => {
                    if (res.code === '200') {
                        this.$message.success("保存成功");
                        // 触发父级更新User的方法(在Manage.vue中)
                        this.$emit("refreshUser");
                        // 更新浏览器存储的用户信息
                        this.getUser().then(res => {
                            res.token = JSON.parse(localStorage.getItem("person")).token;
                            localStorage.setItem("person", JSON.stringify(res))
                        })
                    } else {
                        this.$message.error("保存失败")
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>