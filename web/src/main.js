import { createApp } from "vue";
import "./style.css";
import "vant/lib/index.css";
import App from "./App.vue";
import { Button, Field, CellGroup, Toast } from "vant";

const app = createApp(App);
app.use(Button).use(Field).use(CellGroup).use(Toast);
app.mount("#app");
