<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <h1> Welcome to Health Tracker App</h1>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Activities Tracker</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <h5 class="card-header">Diet Tracker</h5>
        <div class="card-body">
          <h5 class="card-title">{{diets.length}} diets</h5>
          <a href="/diets" class="btn btn-primary">More Details...</a>
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <h5 class="card-header">Sleep Tracker</h5>
        <div class="card-body">
          <h5 class="card-title">{{sleeps.length}} dozes</h5>
          <a href="/sleeps" class="btn btn-primary">More Details...</a>
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <h5 class="card-header">Medicine Tracker</h5>
        <div class="card-body">
          <h5 class="card-title">{{medicines.length}} medicines</h5>
          <a href="/medicines" class="btn btn-primary">More Details...</a>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        diets: [],
        sleeps: [],
        medicines: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/diets")
            .then(res => this.diets = res.data)
            .catch(() => alert("Error while fetching diets"));
        axios.get("/api/sleeps")
            .then(res => this.sleeps = res.data)
            .catch(() => alert("Error while fetching sleeps"));
        axios.get("/api/medicines")
            .then(res => this.medicines = res.data)
            .catch(() => alert("Error while fetching medicines"));
      }
    });
</script>