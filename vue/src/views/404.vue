<template>
    <div class="_404">
        <p class="m-0">抱歉，页⾯未找到，
            <a href="javascript:;" @click="goHome">点击回到⾸页</a>
        </p>
    </div>
</template>

<script>
    export default {
        name: "NotFound",
        data(){
            return{
                countDown:5,
                timer:null
            }
        },
        methods:{
            goHome(){
                clearInterval(this.timer);
                this.$router.push('/home');
            }
        },
        created(){

        }
    }
</script>

<style scoped>
    ._404 {
        overflow: hidden;
        height: 100vh;
        background-image: url('../assets/404.png');
        background-size: 100% 100%;
        text-align: center;
        padding-top: 550px;
        font-size: x-large;
    }
    body{
        background-color: #dceebc;
    }
    a{
        color: #010101;
    }
    a:hover{
        color: skyblue;
    }

</style>