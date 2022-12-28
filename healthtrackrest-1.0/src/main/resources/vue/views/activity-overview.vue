<template id="activity-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Activities
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
        <form id="addActivity">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="formData.description" name="description"
                   placeholder="Description" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-duration">Duration</span>
            </div>
            <input type="number" step="any" class="form-control" v-model="formData.duration" name="duration"
                   placeholder="Duration" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-calories">Calories</span>
            </div>
            <input type="number"  class="form-control" v-model="formData.calories" name="calories"
                   placeholder="Calories" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-started">Date-Time</span>
            </div>
            <input type="text" class="form-control" v-model="formData.started" name="started"
                   placeholder="Date-Time" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId">UserId</span>
            </div>
            <input type="number"  class="form-control" v-model="formData.userId" name="userId"
                   placeholder="userId" />
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addActivity()">Add
          Activity</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(activity,index) in activities"
           v-bind:key="index">
        <div class="mr-auto p-2">

          <span><a :href="`/activities/${activity.id}`"> Description:{{ activity.description }}, Duration:({{
              activity.duration}}), Calories:({{activity.calories}}), Start-Time:({{activity.started}}), User-Id:({{activity.userId}})
                        </a></span>
        </div>
        <div class="p2">
          <a :href="`/activities/${activity.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteActivity(activity, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>
<script>
Vue.component("activity-overview", {
  template: "#activity-overview",
  data: () => ({
    users: [],
    activities: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchUsers();
    this.fetchActivities();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    fetchActivities: function () {
      axios.get("/api/activities")
          .then(res => this.activities = res.data)
          .catch(() => alert("Error while fetching activities"));
    },
    deleteActivity: function (activity, index) {
      if (confirm('Are you sure you want to delete this activity? This action cannot be undone.', 'Warning')) {
        //activity confirmed delete
        const activityId = activity.id;
        const url = `/api/activities/${activityId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addActivity: function () {
      const url = `/api/activities`;
      axios.post(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            calories: this.formData.calories,
            started: this.formData.started,
            userId: this.formData.userId

          })
          .then(response => {
            this.activities.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>