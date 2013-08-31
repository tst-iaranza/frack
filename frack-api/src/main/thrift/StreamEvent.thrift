namespace java ezbake.frack.core.data.thrift

struct StreamEvent {
	1: required string dateTime; // The date time for the stream event captured in ISO 8601 YYYY-MM-DDThh:mm:ssTZD (see: http://www.w3.org/TR/NOTE-datetime)
	2: optional string authorization; // The authorization of the particular event
	3: optional string origin; // Where the event came from
	4: required binary content; // The content of the particular event
}
