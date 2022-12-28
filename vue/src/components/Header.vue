<template>
    <div style="line-height: 60px; display: flex">
        <div style="flex: 1;">
            <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>

            <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
                <el-breadcrumb-item :to="'/home'">首页</el-breadcrumb-item>
                <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-dropdown style="width: 100px; cursor: pointer">
            <div style="display: inline-block">
                <img src="../assets/avatar.png" alt=""
                     style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
                <span>{{ person.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
                <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="person.role === 'INFORMER'">
                    <i class="el-icon-user"></i>
                    <span style="text-decoration: none" @click="personInformation">个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <i class="el-icon-document"></i>
                    <span style="text-decoration: none" @click="changePassword">修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <i class="el-icon-refresh-right"></i>
                    <span style="text-decoration: none" @click="logout">退出登录</span>
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
    export default {
        name: "Header",
        props: {
            collapseBtnClass: String,
        },
        data() {
            return {
                person: localStorage.getItem("person") ? JSON.parse(localStorage.getItem("person")) : {},
            }
        },
        computed: {
            currentPathName() {
                return this.$store.state.currentPathName;　　//需要监听的数据
            },
        },
        watch: {
            currentPathName(newVal, oldVal) {
                console.log(newVal)
            }
        },
        methods: {
            collapse() {
                this.$emit("asideCollapse")
            },
            logout() {
                /*localStorage.removeItem("person");
                localStorage.removeItem("student");*/
                localStorage.clear();
                this.$router.push("/login");
                this.$message.success("退出成功");
            },
            changePassword() {
                this.$router.push("/password");
            },
            personInformation() {
                this.$router.push("/personInformation")
            }
        }
    }
</script>

<style scoped>

</style>