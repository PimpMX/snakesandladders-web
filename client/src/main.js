import { createApp } from 'vue';
import { createVuetify } from 'vuetify';
import App from './App.vue';
import router from './router';
import 'vuetify/styles';
import '@mdi/font/css/materialdesignicons.css';
import wb from "./registerServiceWorker";

const vuetify = createVuetify();

const app = createApp(App);
app.config.globalProperties.$workbox = wb;
app.use(router);
app.use(vuetify);
app.mount('#app');


