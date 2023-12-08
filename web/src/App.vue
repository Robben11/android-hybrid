<template>
  <van-cell-group>
    <van-field v-model="formData.key" label="key" />
    <van-field v-model="formData.value" label="value" />
    <van-button
      type="primary"
      block
      @click="() => handleSend()"
      style="margin-top: 10px"
      >注入函数发送</van-button
    >
    <van-button
      type="primary"
      block
      @click="() => handleAlert()"
      style="margin-top: 10px"
      >Alert发送</van-button
    >
    <van-button
      type="primary"
      block
      @click="() => handleChangeUrl()"
      style="margin-top: 10px"
      >url发送</van-button
    >
  </van-cell-group>
</template>
<script setup>
import { reactive, onMounted } from "vue";
import { showToast } from "vant";

const formData = reactive({
  key: "key",
  value: "value",
  resStr: "",
});

const handleSend = () => {
  window.android.jsFunction(formData.key, formData.value, "callByAndroid");
};

const handleAlert = () => {
  alert(
    JSON.stringify({
      key: formData.key,
      value: formData.value,
      callback: "callByAndroid",
    })
  );
};

const handleChangeUrl = () => {
  document.location = `native://www.native.com?key=${formData.key}&value=${formData.value}&callback=callByAndroid`;
  // document.location = `https://h5-cdn.58.com/git/hrg-fe/zp-ganji/zp-gj-resume/app-resume-existing-user/index.html`;
};

window.callByAndroid = (param) => {
  console.log(param);
  showToast(param);
  return "123123123";
};

onMounted(() => {
  console.log(window.android);
});
</script>

<style scoped>
.value-input {
  display: block;
  width: 100%;
  color: #323233;
  border: 0;
  height: 40px;
  margin-bottom: 10px;
  border-radius: 4px;
  padding: 0 5px;
}
.value-result {
  display: block;
  width: 100%;
  color: #323233;
  border: 0;
  height: 200px;
  margin-top: 10px;
  background-color: #fff;
  padding: 5px;
  border-radius: 4px;
}
</style>
