App.TopicStat = Em.Object.extend({
    topic : null,
    throughputPerSec : 0,
    subscribersNumber : 0,
    subTopicsNumber : 0,
    timestamp : 0
});

App.TopicStatContainer = Em.Object.extend({
    topic : null,
    stats : []
});