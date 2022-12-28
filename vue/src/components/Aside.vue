<template>
    <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
             background-color="rgb(238,238,238)"
             text-color="black"
             active-text-color="#ffd04b"
             :collapse-transition="false"
             :collapse="isCollapse"
             :router="true"
    >
        <div style="height: 60px; line-height: 60px; text-align: center">
            <img src="../assets/logo1.png" alt="" style="width: 40px; position: relative; top: 10px; ">
            <b style="color: black" v-show="logoTextShow">信息员平台</b>
        </div>
        <el-menu-item index="/home">
            <i class="el-icon-house"></i>
            <span slot="title">主页</span>
        </el-menu-item>

        <el-menu-item index="/noticeShow" v-if="person.role==='INFORMER'">
            <i class="el-icon-document"></i>
            <span slot="title">公告展示</span>
        </el-menu-item>

        <el-menu-item index="/attendanceSubmit" v-if="person.role==='INFORMER'">
            <i class="el-icon-position"></i>
            <span slot="title">出勤反馈</span>
        </el-menu-item>

        <el-menu-item index="/informationSubmit" v-if="person.role==='INFORMER'">
            <i class="el-icon-notebook-2"></i>
            <span slot="title">信息反馈</span>
        </el-menu-item>

        <el-menu-item index="/notice" v-if="person.role==='ADMIN'">
            <i class="el-icon-document"></i>
            <span slot="title">公告管理</span>
        </el-menu-item>

        <el-submenu index="2-1" v-if="person.role==='ADMIN'">
            <template slot="title">
                <i class="el-icon-setting"></i>
                <span slot="title">系统设置</span>
            </template>
            <el-menu-item index="/campus">
                <i class="el-icon-school"></i>
                <span slot="title">校区设置</span>
            </el-menu-item>
            <el-menu-item index="/semester">
                <i class="el-icon-tickets"></i>
                <span slot="title">学期设置</span>
            </el-menu-item>

            <el-menu-item index="/course">
                <i class="el-icon-set-up"></i>
                <span slot="title">课程设置</span>
            </el-menu-item>
        </el-submenu>

        <el-submenu index="2-2" v-if="person.role==='ADMIN'">
            <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">人员管理</span>
            </template>
            <el-menu-item index="/user">
                <i class="el-icon-s-custom"></i>
                <span slot="title">信息员管理</span>
            </el-menu-item>
            <el-menu-item index="/admin">
                <i class="el-icon-s-custom"></i>
                <span slot="title">管理员管理</span>
            </el-menu-item>
        </el-submenu>

        <el-submenu index="2-3" v-if="person.role==='ADMIN'">
            <template slot="title">
                <i class="el-icon-data-analysis"></i>
                <span slot="title">数据统计</span>
            </template>
            <el-menu-item index="/attendance">
                <i class="el-icon-position"></i>
                <span slot="title">出勤反馈统计</span>
            </el-menu-item>
            <el-menu-item index="/information">
                <i class="el-icon-notebook-2"></i>
                <span slot="title">信息反馈统计</span>
            </el-menu-item>

            <el-menu-item index="/informerStatistics">
                <i class="el-icon-user"></i>
                <span slot="title">信息员统计</span>
            </el-menu-item>
        </el-submenu>


    </el-menu>
</template>

<script>
    export default {
        name: "Aside",
        props: {
            isCollapse: Boolean,
            logoTextShow: Boolean
        },
        data() {
            return {
                person: localStorage.getItem("person") ? JSON.parse(localStorage.getItem("person")) : {},
            }
        }
    }
</script>

<style>
    /*解决收缩菜单文字不消失问题*/
    .el-menu--collapse span {
        visibility: hidden;
    }
</style>