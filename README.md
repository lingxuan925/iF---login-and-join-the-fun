# iF — login and join the fun

Welcome to our open source Android app! "iF" is a convenient event management app, although is still a prototype, it's main functionality is implemented. 

"iF" helps event creator manage their events as well as check event participants information online. For the participants, they can search nearby events, search events in a particular area or search by both category and area (You can be both event creator and participant). All participants can only login through their Google account so far, when loign completed, users can change their nick name, gender, avatar and more public and private info.

For convenience, we used some open source libraries, they look like this in graddle:
```
implementation 'de.hdodenhof:circleimageview:2.2.0'
implementation 'com.github.yalantis:ucrop:2.2.2'
implementation 'com.fxn769:pix:1.2.5'
```
The instruction of these libraries can be found here:

https://github.com/hdodenhof/CircleImageView

https://github.com/Yalantis/uCrop

https://github.com/akshay2211/PixImagePicker

We used google Api client and firebase to support some functions in our app.

## Features
- Google login logout
- Change avatar (from camera or alberm, crop new avatar image)
- Change nickname, gender, "what's up", age
- Change search radius on "Settings" page (we will add more settings such as change theme, change personal interests)
- Send email to us on "FeedBack" page
- Search events in a specific area
- Show events nearby
- go to current location
- Switch between map view and list view
- Show events by category (Entertainment, Foodies, Sports, Travel) within your chosen radius
- Unjoin an events (if you are the event creator, you can only unjoin it when there are no participants in your event. After the creator unjoined, this event is deleted)
- Show event info(event name, description, number of participants, number of participants capacity, address, date and time) as well as creator info (avatar, name) when you click on the event icon on map, list or upcomming events.
- Show creator info in detail (name, age, gender, "what's up") when you click on the creator's avatar
- Send email to the creator on the creator info detail page
- Outdated event will not show on the map and MyEvents anymore, but there is a history in our database(Firebase)

## Graphic examaple


