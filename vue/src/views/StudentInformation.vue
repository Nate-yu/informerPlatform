<template>
    <el-card style="width: 500px;">
        <el-form label-width="80px" size="small">
            <el-form-item label="姓名" style="width: 100%" prop="name">
                {{ form.name }}
            </el-form-item>
            <el-form-item label="性别" prop="sex">
                {{ form.sex }}
            </el-form-item>
            <el-form-item label="学号" style="width: 100%" prop="stuNum">
                {{ form.stuNum }}
            </el-form-item>
            <el-form-item label="学院" style="width: 100%" prop="college">
                {{ form.college }}
            </el-form-item>
            <el-form-item label="专业" style="width: 100%" prop="major">
                {{ form.major }}
            </el-form-item>
            <el-form-item label="班级" style="width: 100%" prop="classes">
                {{ form.classes }}
            </el-form-item>
            <el-form-item label="学历" style="width: 100%" prop="proof">
                {{ form.proof }}
            </el-form-item>
            <el-form-item label="电话" style="width: 100%" prop="phone">
                {{ form.phone }}
            </el-form-item>
            <el-form-item label="邮箱" style="width: 100%">
                {{ form.email }}
            </el-form-item>
            <el-form-item label="QQ号" style="width: 100%">
                {{ form.qq }}
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
    import request from "../utils/request";
    export default {
        name: "StudentInformation",
        data() {
            return {
                form: {},
                student: localStorage.getItem("student") ? JSON.parse(localStorage.getItem("student")) : {}
            }
        },
        created() {
            this.getUser().then(res => {
                this.form = res
            })
        },
        methods: {
            async getUser() {
                return (await request.get("/user/name/" + this.student.name)).data
            },
        }
    }
</script>

<style scoped>

</style>