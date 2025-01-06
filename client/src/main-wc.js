import { createApp } from 'vue';
import { defineCustomElement } from 'vue';
import App from './App.vue';
import router from './router';  // Import your router setup
import { createVuetify } from 'vuetify';
import 'vuetify/styles';
import '@mdi/font/css/materialdesignicons.css';

const vuetify = createVuetify();
const customElement = defineCustomElement(App);
const app = createApp(App);
app.use(router);
app.use(vuetify);
app.mount('#app');
customElements.define('snakes-and-ladders-app', customElement);