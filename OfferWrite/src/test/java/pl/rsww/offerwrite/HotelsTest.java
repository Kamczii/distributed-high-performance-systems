package pl.rsww.offerwrite;

import pl.rsww.offerwrite.api.requests.HotelRequests.CreateHotel;
import pl.rsww.offerwrite.api.requests.HotelRequests.LocationRequest;
import pl.rsww.offerwrite.api.requests.HotelRequests.RoomRequest;

import java.util.Arrays;
import java.util.UUID;

public class HotelsTest {
    public static CreateHotel[] test() {
        return new CreateHotel[]{
                new CreateHotel(UUID.fromString("0c52fcb1-a888-43ab-9129-80462dedbd96"),
                        new LocationRequest("Country16", "City154"),
                        Arrays.asList(new RoomRequest("Double", 1), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Double", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 2), new RoomRequest("Single", 1), new RoomRequest("Single", 1), new RoomRequest("Single", 2), new RoomRequest("Suite", 1), new RoomRequest("Single", 2), new RoomRequest("Double", 4), new RoomRequest("Single", 1), new RoomRequest("Single", 3), new RoomRequest("Double", 3))),
                new CreateHotel(UUID.fromString("a31b60b7-471e-4922-8464-3daa3747f4cf"),
                        new LocationRequest("Country16", "City22"),
                        Arrays.asList(new RoomRequest("Suite", 1), new RoomRequest("Double", 1), new RoomRequest("Double", 4), new RoomRequest("Suite", 2), new RoomRequest("Double", 2), new RoomRequest("Double", 2))),
                new CreateHotel(UUID.fromString("ffeca2dd-3471-4836-a0e9-02d661fcd627"),
                        new LocationRequest("Country20", "City153"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Single", 4), new RoomRequest("Double", 4), new RoomRequest("Double", 3), new RoomRequest("Double", 3), new RoomRequest("Double", 3), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Double", 1), new RoomRequest("Suite", 1), new RoomRequest("Double", 3), new RoomRequest("Double", 1), new RoomRequest("Single", 4), new RoomRequest("Suite", 3), new RoomRequest("Double", 2), new RoomRequest("Double", 4))),
                new CreateHotel(UUID.fromString("881865a1-72fa-41e4-97dd-e5dddc4ac3d7"),
                        new LocationRequest("Country9", "City114"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Single", 2), new RoomRequest("Suite", 4), new RoomRequest("Single", 3), new RoomRequest("Double", 2), new RoomRequest("Single", 2), new RoomRequest("Suite", 3), new RoomRequest("Suite", 2), new RoomRequest("Double", 4))),
                new CreateHotel(UUID.fromString("d27269ab-b5de-4c1b-940e-bcf239c0a0e2"),
                        new LocationRequest("Country9", "City40"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Suite", 2), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Double", 4), new RoomRequest("Double", 4), new RoomRequest("Double", 4), new RoomRequest("Single", 2), new RoomRequest("Double", 3), new RoomRequest("Double", 4), new RoomRequest("Single", 4), new RoomRequest("Double", 2), new RoomRequest("Double", 4), new RoomRequest("Single", 3), new RoomRequest("Single", 2))),
                new CreateHotel(UUID.fromString("97a958e2-6d69-4a6c-bae0-12aae252e0bf"),
                        new LocationRequest("Country6", "City20"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Double", 3), new RoomRequest("Double", 1), new RoomRequest("Suite", 2), new RoomRequest("Double", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 4), new RoomRequest("Suite", 4))),
                new CreateHotel(UUID.fromString("bede0006-958a-4868-b09d-73c814665e38"),
                        new LocationRequest("Country16", "City122"),
                        Arrays.asList(new RoomRequest("Suite", 1), new RoomRequest("Double", 4), new RoomRequest("Single", 2), new RoomRequest("Single", 3), new RoomRequest("Double", 4), new RoomRequest("Double", 3), new RoomRequest("Suite", 1), new RoomRequest("Suite", 3))),
                new CreateHotel(UUID.fromString("f397b263-ad6e-4e38-819c-05d1a0c80baf"),
                        new LocationRequest("Country14", "City175"),
                        Arrays.asList(new RoomRequest("Single", 1), new RoomRequest("Single", 1), new RoomRequest("Double", 1), new RoomRequest("Single", 2), new RoomRequest("Single", 1), new RoomRequest("Suite", 3), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Single", 2), new RoomRequest("Suite", 1), new RoomRequest("Double", 3), new RoomRequest("Single", 4), new RoomRequest("Double", 3), new RoomRequest("Suite", 1))),
                new CreateHotel(UUID.fromString("6ac6f2bd-376b-47f8-aeec-d94e942504d1"),
                        new LocationRequest("Country3", "City166"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 2), new RoomRequest("Single", 4), new RoomRequest("Double", 1), new RoomRequest("Double", 1), new RoomRequest("Single", 3), new RoomRequest("Double", 4), new RoomRequest("Suite", 2), new RoomRequest("Suite", 2), new RoomRequest("Single", 4), new RoomRequest("Single", 2), new RoomRequest("Double", 4), new RoomRequest("Double", 4), new RoomRequest("Single", 3))),
                new CreateHotel(UUID.fromString("b09639c2-3854-4ec2-a3e8-44a80017db79"),
                        new LocationRequest("Country10", "City34"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Single", 4), new RoomRequest("Suite", 3), new RoomRequest("Single", 2), new RoomRequest("Double", 4), new RoomRequest("Double", 4), new RoomRequest("Single", 4))),
                new CreateHotel(UUID.fromString("5034f832-f4f3-4338-86e5-6e110b6bc681"),
                        new LocationRequest("Country13", "City75"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Suite", 1), new RoomRequest("Suite", 4), new RoomRequest("Single", 4), new RoomRequest("Suite", 2), new RoomRequest("Single", 2), new RoomRequest("Double", 1))),
                new CreateHotel(UUID.fromString("505199ff-4d6e-4f3e-be71-09d88122732b"),
                        new LocationRequest("Country20", "City162"),
                        Arrays.asList(new RoomRequest("Suite", 3), new RoomRequest("Single", 3), new RoomRequest("Suite", 1), new RoomRequest("Suite", 3), new RoomRequest("Single", 4), new RoomRequest("Double", 2), new RoomRequest("Suite", 1), new RoomRequest("Single", 4), new RoomRequest("Suite", 2), new RoomRequest("Single", 3), new RoomRequest("Suite", 1), new RoomRequest("Double", 1), new RoomRequest("Double", 1), new RoomRequest("Single", 2), new RoomRequest("Single", 3))),
                new CreateHotel(UUID.fromString("be916ec1-6657-4f7e-92fa-dd5f11ccf193"),
                        new LocationRequest("Country20", "City172"),
                        Arrays.asList(new RoomRequest("Suite", 3), new RoomRequest("Suite", 2), new RoomRequest("Single", 4), new RoomRequest("Single", 2), new RoomRequest("Single", 3))),
                new CreateHotel(UUID.fromString("e528f503-aab8-4013-bce5-6766afc82840"),
                        new LocationRequest("Country14", "City95"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Suite", 2), new RoomRequest("Double", 3), new RoomRequest("Suite", 1), new RoomRequest("Suite", 3), new RoomRequest("Suite", 3))),
                new CreateHotel(UUID.fromString("0b8c0fae-ead0-405e-b1ed-82f527ce8568"),
                        new LocationRequest("Country3", "City183"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Suite", 1), new RoomRequest("Double", 4), new RoomRequest("Double", 1), new RoomRequest("Single", 1), new RoomRequest("Single", 1), new RoomRequest("Double", 4), new RoomRequest("Single", 1), new RoomRequest("Suite", 1), new RoomRequest("Single", 1))),
                new CreateHotel(UUID.fromString("c2d1f84a-0545-474f-93f8-48f72cbe6b3c"),
                        new LocationRequest("Country9", "City126"),
                        Arrays.asList(new RoomRequest("Suite", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 1), new RoomRequest("Double", 2), new RoomRequest("Single", 2), new RoomRequest("Double", 3), new RoomRequest("Double", 3), new RoomRequest("Suite", 1), new RoomRequest("Suite", 1), new RoomRequest("Single", 4))),
                new CreateHotel(UUID.fromString("b99c9a4b-4748-4fd3-ad07-36c64ffcf25c"),
                        new LocationRequest("Country2", "City16"),
                        Arrays.asList(new RoomRequest("Double", 1), new RoomRequest("Suite", 4), new RoomRequest("Double", 2), new RoomRequest("Double", 2), new RoomRequest("Suite", 3), new RoomRequest("Suite", 3), new RoomRequest("Double", 1), new RoomRequest("Suite", 2), new RoomRequest("Double", 4), new RoomRequest("Suite", 2), new RoomRequest("Suite", 2), new RoomRequest("Suite", 3), new RoomRequest("Suite", 4), new RoomRequest("Single", 1), new RoomRequest("Suite", 1))),
                new CreateHotel(UUID.fromString("43d6aa4b-8d24-4c7c-b040-1d10ab57446e"),
                        new LocationRequest("Country19", "City167"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Double", 3), new RoomRequest("Single", 1), new RoomRequest("Suite", 3), new RoomRequest("Single", 1), new RoomRequest("Suite", 1), new RoomRequest("Double", 4), new RoomRequest("Suite", 3), new RoomRequest("Suite", 1), new RoomRequest("Double", 3), new RoomRequest("Single", 4), new RoomRequest("Suite", 4), new RoomRequest("Double", 1), new RoomRequest("Suite", 4))),
                new CreateHotel(UUID.fromString("88553dd0-7c92-40da-a18e-f5ca9e45e0a5"),
                        new LocationRequest("Country7", "City82"),
                        Arrays.asList(new RoomRequest("Single", 2), new RoomRequest("Suite", 2), new RoomRequest("Suite", 2), new RoomRequest("Double", 4), new RoomRequest("Suite", 3), new RoomRequest("Suite", 3), new RoomRequest("Suite", 2), new RoomRequest("Suite", 2), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Double", 3), new RoomRequest("Double", 4), new RoomRequest("Suite", 4), new RoomRequest("Single", 4), new RoomRequest("Double", 3), new RoomRequest("Suite", 3), new RoomRequest("Single", 3), new RoomRequest("Suite", 1), new RoomRequest("Single", 1), new RoomRequest("Suite", 1))),
                new CreateHotel(UUID.fromString("836e5916-4ca5-4e5e-a952-db72e5bebf7b"),
                        new LocationRequest("Country16", "City36"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Suite", 4), new RoomRequest("Double", 4), new RoomRequest("Double", 1), new RoomRequest("Single", 2), new RoomRequest("Double", 4), new RoomRequest("Single", 3), new RoomRequest("Single", 2))),
                new CreateHotel(UUID.fromString("3286f316-404f-45fe-b6b3-28bae2b5be81"),
                        new LocationRequest("Country17", "City77"),
                        Arrays.asList(new RoomRequest("Single", 1), new RoomRequest("Suite", 3), new RoomRequest("Double", 4), new RoomRequest("Double", 1), new RoomRequest("Single", 3), new RoomRequest("Suite", 1), new RoomRequest("Suite", 3), new RoomRequest("Single", 2), new RoomRequest("Single", 1), new RoomRequest("Double", 4))),
                new CreateHotel(UUID.fromString("cdac46f0-c5cb-4279-8f6b-a2f0c8aec7e5"),
                        new LocationRequest("Country17", "City44"),
                        Arrays.asList(new RoomRequest("Single", 1), new RoomRequest("Double", 1), new RoomRequest("Double", 4), new RoomRequest("Suite", 2), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Single", 3), new RoomRequest("Single", 1), new RoomRequest("Single", 1), new RoomRequest("Double", 2), new RoomRequest("Suite", 2), new RoomRequest("Single", 2), new RoomRequest("Single", 2), new RoomRequest("Double", 4), new RoomRequest("Double", 3), new RoomRequest("Double", 1), new RoomRequest("Single", 1))),
                new CreateHotel(UUID.fromString("8afbf689-1122-4042-9ecc-b963796d5747"),
                        new LocationRequest("Country7", "City151"),
                        Arrays.asList(new RoomRequest("Suite", 2), new RoomRequest("Single", 2), new RoomRequest("Suite", 3), new RoomRequest("Suite", 1), new RoomRequest("Double", 1), new RoomRequest("Double", 4), new RoomRequest("Double", 3), new RoomRequest("Single", 1), new RoomRequest("Suite", 2), new RoomRequest("Suite", 4), new RoomRequest("Suite", 1), new RoomRequest("Double", 3), new RoomRequest("Single", 3), new RoomRequest("Single", 3))),
                new CreateHotel(UUID.fromString("a46f5afa-d987-4541-8054-d149d6d5b496"),
                        new LocationRequest("Country10", "City114"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Suite", 3), new RoomRequest("Single", 4), new RoomRequest("Suite", 3), new RoomRequest("Suite", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 4), new RoomRequest("Double", 4), new RoomRequest("Suite", 1), new RoomRequest("Suite", 4), new RoomRequest("Suite", 2), new RoomRequest("Suite", 1), new RoomRequest("Single", 4))),
                new CreateHotel(UUID.fromString("2c261654-69f4-4ce2-8512-f06dba0b46bc"),
                        new LocationRequest("Country13", "City41"),
                        Arrays.asList(new RoomRequest("Double", 1), new RoomRequest("Single", 4), new RoomRequest("Single", 1), new RoomRequest("Double", 3), new RoomRequest("Suite", 3), new RoomRequest("Suite", 2), new RoomRequest("Double", 4), new RoomRequest("Single", 3), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Double", 4), new RoomRequest("Double", 4), new RoomRequest("Single", 3), new RoomRequest("Double", 2), new RoomRequest("Suite", 1), new RoomRequest("Single", 4), new RoomRequest("Single", 4), new RoomRequest("Double", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 3))),
                new CreateHotel(UUID.fromString("d61c0108-fcb1-4db8-a109-aba6aceaef39"),
                        new LocationRequest("Country3", "City118"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Double", 2), new RoomRequest("Single", 2), new RoomRequest("Suite", 3), new RoomRequest("Single", 4), new RoomRequest("Suite", 3), new RoomRequest("Single", 3), new RoomRequest("Suite", 2), new RoomRequest("Double", 3), new RoomRequest("Double", 4), new RoomRequest("Single", 1), new RoomRequest("Single", 1), new RoomRequest("Single", 4), new RoomRequest("Single", 3))),
                new CreateHotel(UUID.fromString("e3e51b1e-5beb-4cdc-b776-619debacee9f"),
                        new LocationRequest("Country9", "City132"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Suite", 2), new RoomRequest("Double", 1), new RoomRequest("Single", 4), new RoomRequest("Suite", 4), new RoomRequest("Suite", 1))),
                new CreateHotel(UUID.fromString("b6b779eb-cd78-4de3-b567-9a00d4ff5c51"),
                        new LocationRequest("Country17", "City28"),
                        Arrays.asList(new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Single", 1), new RoomRequest("Suite", 2), new RoomRequest("Suite", 1), new RoomRequest("Suite", 4), new RoomRequest("Suite", 2), new RoomRequest("Suite", 4), new RoomRequest("Double", 2), new RoomRequest("Single", 2), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Double", 3), new RoomRequest("Single", 3))),
                new CreateHotel(UUID.fromString("d8b618c0-49ff-4b39-992a-c2329a727a7d"),
                        new LocationRequest("Country8", "City4"),
                        Arrays.asList(new RoomRequest("Suite", 2), new RoomRequest("Single", 1), new RoomRequest("Single", 4), new RoomRequest("Double", 3), new RoomRequest("Suite", 2), new RoomRequest("Single", 1), new RoomRequest("Single", 4), new RoomRequest("Double", 3), new RoomRequest("Single", 2), new RoomRequest("Suite", 2), new RoomRequest("Single", 1), new RoomRequest("Suite", 1), new RoomRequest("Suite", 4), new RoomRequest("Suite", 3), new RoomRequest("Single", 3), new RoomRequest("Single", 2), new RoomRequest("Double", 1), new RoomRequest("Double", 1), new RoomRequest("Single", 3), new RoomRequest("Single", 1))),
                new CreateHotel(UUID.fromString("3182bf25-1d7f-4880-88e9-27cddb177a3b"),
                        new LocationRequest("Country11", "City164"),
                        Arrays.asList(new RoomRequest("Double", 1), new RoomRequest("Double", 4), new RoomRequest("Suite", 3), new RoomRequest("Single", 2), new RoomRequest("Double", 3), new RoomRequest("Double", 3), new RoomRequest("Single", 2), new RoomRequest("Suite", 1), new RoomRequest("Suite", 2), new RoomRequest("Double", 4), new RoomRequest("Suite", 4), new RoomRequest("Suite", 3), new RoomRequest("Suite", 2), new RoomRequest("Single", 4), new RoomRequest("Suite", 3), new RoomRequest("Suite", 1), new RoomRequest("Suite", 3), new RoomRequest("Suite", 3))),
                new CreateHotel(UUID.fromString("8a0ce93f-8d2b-40ab-bcc9-a8be2f8a5be2"),
                        new LocationRequest("Country3", "City75"),
                        Arrays.asList(new RoomRequest("Single", 2), new RoomRequest("Suite", 2), new RoomRequest("Double", 1), new RoomRequest("Single", 2), new RoomRequest("Single", 1), new RoomRequest("Double", 1), new RoomRequest("Double", 2), new RoomRequest("Suite", 1), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 4))),
                new CreateHotel(UUID.fromString("7191d7a4-d9f7-4f61-af0e-a359aca95651"),
                        new LocationRequest("Country14", "City94"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Single", 2), new RoomRequest("Double", 4), new RoomRequest("Suite", 4), new RoomRequest("Single", 1), new RoomRequest("Suite", 2), new RoomRequest("Suite", 2), new RoomRequest("Double", 1))),
                new CreateHotel(UUID.fromString("79ba8daa-0cf9-4db5-a632-68106b76892a"),
                        new LocationRequest("Country8", "City199"),
                        Arrays.asList(new RoomRequest("Single", 2), new RoomRequest("Double", 2), new RoomRequest("Single", 2), new RoomRequest("Suite", 4), new RoomRequest("Suite", 2), new RoomRequest("Single", 2))),
                new CreateHotel(UUID.fromString("dfea2441-e4b2-4308-9f2e-69eba9e2523d"),
                        new LocationRequest("Country6", "City122"),
                        Arrays.asList(new RoomRequest("Double", 4), new RoomRequest("Double", 2), new RoomRequest("Suite", 4), new RoomRequest("Suite", 2), new RoomRequest("Double", 2), new RoomRequest("Suite", 4), new RoomRequest("Single", 1), new RoomRequest("Single", 2), new RoomRequest("Suite", 2), new RoomRequest("Single", 1), new RoomRequest("Double", 2), new RoomRequest("Single", 3))),
                new CreateHotel(UUID.fromString("dfba3e41-2235-4d21-a2b5-48e9acccaf5a"),
                        new LocationRequest("Country8", "City125"),
                        Arrays.asList(new RoomRequest("Suite", 4), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Suite", 1), new RoomRequest("Double", 1), new RoomRequest("Suite", 1), new RoomRequest("Double", 1), new RoomRequest("Single", 1), new RoomRequest("Double", 1), new RoomRequest("Single", 3), new RoomRequest("Double", 4), new RoomRequest("Single", 4), new RoomRequest("Suite", 1), new RoomRequest("Single", 4), new RoomRequest("Double", 1), new RoomRequest("Suite", 3), new RoomRequest("Single", 3), new RoomRequest("Double", 4), new RoomRequest("Suite", 4), new RoomRequest("Suite", 3))),
                new CreateHotel(UUID.fromString("14f8b616-2839-4289-8e76-b0942af2fddf"),
                        new LocationRequest("Country15", "City178"),
                        Arrays.asList(new RoomRequest("Suite", 4), new RoomRequest("Single", 1), new RoomRequest("Suite", 4), new RoomRequest("Single", 4), new RoomRequest("Single", 2), new RoomRequest("Suite", 1), new RoomRequest("Single", 3), new RoomRequest("Suite", 4), new RoomRequest("Suite", 1), new RoomRequest("Suite", 4), new RoomRequest("Single", 2), new RoomRequest("Double", 2), new RoomRequest("Double", 2))),
                new CreateHotel(UUID.fromString("218fd6f8-a9d1-4ca9-a5fa-b5bd2e163941"),
                        new LocationRequest("Country5", "City40"),
                        Arrays.asList(new RoomRequest("Suite", 3), new RoomRequest("Suite", 4), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Double", 3))),
                new CreateHotel(UUID.fromString("be19fdec-9338-47a2-803a-70f364bd6fda"),
                        new LocationRequest("Country9", "City181"),
                        Arrays.asList(new RoomRequest("Single", 1), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Double", 4), new RoomRequest("Single", 1), new RoomRequest("Suite", 1), new RoomRequest("Double", 3))),
                new CreateHotel(UUID.fromString("28e2455c-f380-4b74-89d3-53d6be580dd2"),
                        new LocationRequest("Country2", "City143"),
                        Arrays.asList(new RoomRequest("Single", 4), new RoomRequest("Suite", 2), new RoomRequest("Suite", 3), new RoomRequest("Single", 2), new RoomRequest("Single", 4), new RoomRequest("Suite", 3), new RoomRequest("Suite", 2), new RoomRequest("Suite", 4), new RoomRequest("Double", 2), new RoomRequest("Double", 3), new RoomRequest("Single", 4), new RoomRequest("Single", 4), new RoomRequest("Single", 2), new RoomRequest("Suite", 4), new RoomRequest("Single", 1), new RoomRequest("Suite", 3), new RoomRequest("Double", 4), new RoomRequest("Single", 4), new RoomRequest("Double", 1))),
                new CreateHotel(UUID.fromString("c6cd2b4c-669b-432a-94b6-51c813bb59d0"),
                        new LocationRequest("Country10", "City34"),
                        Arrays.asList(new RoomRequest("Single", 2), new RoomRequest("Double", 2), new RoomRequest("Single", 3), new RoomRequest("Single", 3), new RoomRequest("Single", 4), new RoomRequest("Suite", 2), new RoomRequest("Single", 4), new RoomRequest("Suite", 2), new RoomRequest("Single", 3), new RoomRequest("Single", 4), new RoomRequest("Single", 4), new RoomRequest("Suite", 4), new RoomRequest("Single", 3), new RoomRequest("Suite", 4))),
        };
    }
}

