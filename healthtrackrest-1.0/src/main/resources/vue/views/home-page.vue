<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home">
  <app-layout>
    <h1> Welcome to Health Tracker App</h1>
  </app-layout>
</template>

<script>
Vue.component('home-page',
    {
      template: "#home-page",
    });
</script>