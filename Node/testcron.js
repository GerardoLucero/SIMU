var Job = require('cron').CronJob;

new Job('*/2 * * * * *', function() {
  console.log(new Date().toString()); }
).start();
