<template>
    <div style="color: #666;font-size: 14px;">
        <div style="padding-bottom: 20px">
            <b>您好！{{ person.name }}</b>
        </div>
        <el-card>
            欢迎使用信息员平台
            <el-divider />
            虎虎生威，虎年大吉
        </el-card>

        <el-card style="margin: 10px 0">
            <div style="padding-bottom: 20px; font-size: 24px">平 台 公 告</div>
            <el-collapse v-model="activeNames" v-for="(item, index) in notice">
                <el-collapse-item :name="index + ''">
                    <template slot="title">
                        <span style="font-size: 20px; color: #E6A23C">{{ item.name }}</span><i style="color: #E6A23C" class="header-icon el-icon-info"></i>
                        <span style="margin-left: 20px">{{ item.createTime }}</span>
                    </template>
                    <div>
                        <div>{{ item.content }}</div>
                    </div>
                </el-collapse-item>
            </el-collapse>
        </el-card>
    </div>
</template>

<script>
    import request from "../utils/request";
    export default {

        name: "Home",
        data() {
            return {
                activeNames: ['0'],
                person: localStorage.getItem("person") ? JSON.parse(localStorage.getItem("person")) : {},
                notice: [],
            }
        },
        created() {
            // 简单公告
            request.get("/notice").then(res => {
                this.notice = res.data.splice(0, 5)
            });
        }
    }
</script>