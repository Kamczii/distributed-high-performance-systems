package pl.rsww.offerwrite.core.events;

public record EventMetadata(
  String eventId,
  long streamPosition,
  long logPosition,
  String eventType
) {
}
