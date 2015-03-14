<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>  -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="//static.opentok.com/webrtc/v2.2/js/opentok.min.js"></script>
<script>
	$(document)
			.ready(
					function() {

						var roles = [ 'Publisher' ];
						$
								.ajax({
									type : "POST",
									//dataType:'json',
									data : {
										rolename : roles
									},
									url : "SessionServelet",
									success : function(data) {

										obj = jQuery.parseJSON(data);
										sessionId = obj.session;
										apikey = obj.apiKey;
										room = obj.room;
										token = obj.token;

										var session;
										var publisher;
										var subscribers = [];
										var subscribes;
										var role = obj.role;

										session = OT.initSession(apikey,
												sessionId);

										var connectionCount = 0;
										session
												.on(
														{

															streamCreated : function(
																	event) {
																console
																		.log(event.streams.length
																				+ "data");
																console
																		.log("role:"
																				+ role);

																// This is another client's stream, so you may want to subscribe to it.  			    				
																for ( var i = 0; i < event.streams.length; i++) {
																	addStream(event.streams[i]);
																}
															},

															archiveStarted : function(
																	event) {
																console
																		.log("Archive Started");
																hide('startArchive');
																show('stopArchive');
															},
															archiveStopped : function(
																	event) {

																console
																		.log("Archive Stoped");
																show('startArchive');
																hide('stopArchive');
															}
														})

												.connect(
														token,
														function(error) {
															if (!error) {
																var parentDiv = document
																		.getElementById('myCamera');
																var publisherDiv = document
																		.createElement('div'); // Create a div for the publisher to replace
																var publisherProperties = {};

																publisherProperties.name = 'Admin';

																publisherDiv
																		.setAttribute(
																				'id',
																				'opentok_publisher');
																parentDiv
																		.appendChild(publisherDiv);

																OT
																		.setLogLevel(OT.WARN);
																publisher = session
																		.publish(
																				publisherDiv.id,
																				null);
																show('startArchive');
															}
														}

												);

										$('#startArchive')
												.on(
														'click',
														function() {

															var actions = [ 'START_ARCHIVE' ];
															$
																	.ajax({
																		type : "POST",
																		data : {
																			ArchiveAction : actions
																		},
																		url : "archive",
																		success : function(
																				data) {
																			obj = jQuery
																					.parseJSON(data);

																			archiveId = obj.archiveId;
																		}

																	})

														});
										$('#stopArchive')
												.on(
														'click',
														function() {
															var actions = [ 'STOP_ARCHIVE',sessionId,archiveId ];
															$
																	.ajax({
																		type : "POST",
																		data : {
																			ArchiveAction : actions
																		},
																		url : "archive",
																		success : function(
																				data) {
																			
																		}

																	})

														});

										//--------------------------------------
										//  HELPER METHODS
										//--------------------------------------

										function addStream(stream) {
											//	console.log("new stream added ");
											// Check if this is the stream that I am publishing, and if so do not publish.
											if (stream.connection.connectionId == session.connection.connectionId) {
												return;
											}
											var subscriberDiv = document
													.createElement('div'); // Create a div for the subscriber to replace
											subscriberDiv.setAttribute('id',
													stream.streamId); // Give the replacement div the id of the stream as its id.
											document.getElementById(
													'subscribers').appendChild(
													subscriberDiv);
											subscribers[stream.streamId] = session
													.subscribe(stream,
															subscriberDiv.id);

										}

										function show(id) {
											document.getElementById(id).style.display = 'block';
										}

										function hide(id) {
											document.getElementById(id).style.display = 'none';
										}

									},
									error : function(e) {
										alert(e);
									}
								});

					});
</script>
<body>
	<div id="myCamera" class="publisherContainer"></div>
	<div id="subscribers"></div>
	<div id="links">
		<input type="button" value="Start Archive" id="startArchive"
			style="display: none" /> <input type="button" value="Stop Archive"
			id="stopArchive" style="display: none" />
	</div>

</body>
</html>