<template id="activity-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user's activity.</p>
      <p> View <a :href="'/activities'">all users activities</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User activity Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link"
                    @click="updateActivity()">
              <i class="fas fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteActivity()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="activity.description" name="description"
                   placeholder="Description" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-duration">Duration</span>
            </div>
            <input type="number" class="form-control" v-model="activity.duration" name="duration"
                   placeholder="Duration" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-calories">Calories</span>
            </div>
            <input type="number" class="form-control" v-model="activity.calories" name="calories"
                   placeholder="Calories" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-started">Date</span>
            </div>
            <input type="text" class="form-control" v-model="activity.started" name="started"
                   placeholder="Date" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId">UserId</span>
            </div>
            <input type="number" class="form-control" v-model="activity.userId" name="userId" readonly
                   placeholder="UserId" />
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
            {{ activity.description }} for {{ activity.duration }} minutes
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("activity-profile", {
  template: "#activity-profile",
  data: () => ({
    activity: null,
    noUserFound: false,
  }),
  created: function () {
    const activityId = this.$javalin.pathParams["activity-id"];
    axios.get(`/api/activities/${activityId}`)
        .then(res => this.activity = res.data)
        .catch(error => {
          console.log("No activity found: " + error)
          this.noUserFound = true
        })
  },
  methods: {
    updateActivity: function () {
      const activityId = this.$javalin.pathParams["activity-id"];
      const url = `/api/activities/${activityId}`
      axios.patch(url,
          {
            description: this.activity.description,
            duration: this.activity.duration,
            calories: this.activity.calories,
            started: this.activity.started,
            userId: this.activity.userId
          })
          .then(response =>
              this.activity.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User activity updated!")
    },
    deleteActivity: function () {
      if (confirm("Do you really want to delete?")) {
        const activityId = this.$javalin.pathParams["activity-id"];
        const url = `/api/activities/${activityId}`
        axios.delete(url)
            .then(response => {
              alert("User activity deleted")
              //display the /activities endpoint
              window.location.href = '/activities';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>