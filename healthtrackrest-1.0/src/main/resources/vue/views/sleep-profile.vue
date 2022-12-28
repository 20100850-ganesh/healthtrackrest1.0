<template id="sleep-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user's sleep profile.</p>
      <p> View <a :href="'/sleeps'">all users sleep profiles</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Sleep Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link"
                    @click="updateSleep()">
              <i class="fas fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteSleep()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-duration">Duration</span>
            </div>
            <input type="number" class="form-control" v-model="sleep.duration" name="duration"
                   placeholder="Duration" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-phase">Phase</span>
            </div>
            <input type="text" class="form-control" v-model="sleep.phase" name="phase"
                   placeholder="Phase" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-day">Day</span>
            </div>
            <input type="text" class="form-control" v-model="sleep.day" name="day" placeholder="Day" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-starttime">Date&Time</span>
            </div>
            <input type="text" class="form-control" v-model="sleep.starttime" name="starttime"
                   placeholder="Date&Time" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId">UserId</span>
            </div>
            <input type="number" class="form-control" v-model="sleep.userId" name="userId" readonly
                   placeholder="UserId" />
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        {{ sleep.duration }} hours on {{ sleep.day }}.
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("sleep-profile", {
  template: "#sleep-profile",
  data: () => ({

    sleep: null,
    noUserFound: false,

  }),
  created: function () {
    const sleepId = this.$javalin.pathParams["sleep-id"];
    axios.get(`/api/sleeps/${sleepId}`)
        .then(res => this.sleep = res.data)
        .catch(error => {
          console.log("No sleep found: " + error)
          this.noUserFound = true
        })

  },
  methods: {
    updateSleep: function () {
      const sleepId = this.$javalin.pathParams["sleep-id"];
      const url = `/api/sleeps/${sleepId}`
      axios.patch(url,
          {
            duration: this.sleep.duration,
            phase: this.sleep.phase,
            day: this.sleep.day,
            starttime: this.sleep.starttime,
            userId: this.sleep.userId
          })
          .then(response =>
              this.sleep.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User sleep updated!")
    },
    deleteSleep: function () {
      if (confirm("Do you really want to delete?")) {
        const sleepId = this.$javalin.pathParams["sleep-id"];
        const url = `/api/sleeps/${sleepId}`
        axios.delete(url)
            .then(response => {
              alert("User sleep deleted")
              //display the /sleeps endpoint
              window.location.href = '/sleeps';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>