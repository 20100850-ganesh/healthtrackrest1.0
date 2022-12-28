<template id="sleep-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Sleep Tracker
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addSleep">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-duration">Duration</span>
            </div>
            <input type="number" step="any" class="form-control" v-model="formData.duration" name="duration"
                   placeholder="Duration" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-phase">Phase</span>
            </div>
            <input type="text" class="form-control" v-model="formData.phase" name="phase"
                   placeholder="Phase" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-day">Day</span>
            </div>
            <input type="text" class="form-control" v-model="formData.day" name="day" placeholder="Day" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-starttime">Date-Time</span>
            </div>
            <input type="text" class="form-control" v-model="formData.starttime" name="starttime"
                   placeholder="Date-Time" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId">UserId</span>
            </div>
            <input type="number"  class="form-control" v-model="formData.userId" name="userId"
                   placeholder="UserId" />
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addSleep()">Add
          Sleep</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(sleep,index) in sleeps" v-bind:key="index">
        <div class="mr-auto p-2">

          <span><a :href="`/sleeps/${sleep.id}`"> Duration(in Hours):{{ sleep.duration }}, Phase:({{
              sleep.phase}}), Day-of-Week:({{sleep.day}}), Date-Time:({{sleep.starttime}}), User-Id:({{sleep.userId}})
                        </a></span>
        </div>
        <div class="p2">
          <a :href="`/sleeps/${sleep.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteSleep(sleep, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>
<script>
Vue.component("sleep-overview", {
  template: "#sleep-overview",
  data: () => ({
    users: [],
    sleeps: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchUsers();
    this.fetchSleeps();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    fetchSleeps: function () {
      axios.get("/api/sleeps")
          .then(res => this.sleeps = res.data)
          .catch(() => alert("Error while fetching sleep record"));
    },
    deleteSleep: function (sleep, index) {
      if (confirm('Are you sure you want to delete this sleep record? This action cannot be undone.', 'Warning')) {
        //sleep confirmed delete
        const sleepId = sleep.id;
        const url = `/api/sleeps/${sleepId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.sleeps.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addSleep: function () {
      const url = `/api/sleeps`;
      axios.post(url,
          {
            duration: this.formData.duration,
            phase: this.formData.phase,
            day: this.formData.day,
            starttime: this.formData.starttime,
            userId: this.formData.userId

          })
          .then(response => {
            this.sleeps.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>