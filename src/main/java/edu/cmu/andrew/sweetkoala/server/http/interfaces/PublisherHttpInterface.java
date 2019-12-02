package edu.cmu.andrew.sweetkoala.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.http.utils.PATCH;
import edu.cmu.andrew.sweetkoala.server.managers.*;
import edu.cmu.andrew.sweetkoala.server.models.*;
import edu.cmu.andrew.sweetkoala.server.utils.*;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Locale;

@Path("/publishers")

public class PublisherHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> publisherCollection = null;

    public PublisherHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postPublishers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Publisher newpublisher = new Publisher(
                    null,
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("username"),
                    json.getString("password"),
                    json.getString("email"),
                    json.getString("phone"),
                    json.getString("location"),
                    json.getString("type_id"),
                    json.getInt("coin")
            );
            PublisherManager.getInstance().createPublisher(newpublisher);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST publishers", e);
        }
    }

    //Sorting: http://localhost:8080/api/publishers?sortby=coin
    //Pagination: http://localhost:8080/api/publishers?offset=1&count=2
    //Filtering: http://localhost:8080/api/publisher?type_id=001
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublishers(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                     @QueryParam("count") Integer count,@QueryParam("type_id") String type_id){
        try{
            AppLogger.info("Got an API call");
            System.out.println("Test2");
            ArrayList<Publisher> publishers = null;

            if(sortby != null)
                publishers = PublisherManager.getInstance().getPublisherListSorted(sortby);
            else if(offset != null && count != null)
                publishers = PublisherManager.getInstance().getPublisherListPaginated(offset, count);
            else if(type_id!=null){
                publishers = PublisherManager.getInstance().getPublisherListFiltered(type_id);
            }
            else
                publishers = PublisherManager.getInstance().getPublisherList();

            System.out.println(publishers.size());
            if(publishers != null)
            {
                System.out.println("Test1");
                return new AppResponse(publishers);
            }

            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers", e);
        }
    }

/*
  //http://server.com/api/publishers?begin=11&count=10
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublishersPaginated(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherList();

            if(publishers != null)
                return new AppResponse(publishers);
            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers", e);
        }
    }*/

    @GET
    @Path("/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSinglePublisher(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherById(publisherId);

            if(publishers != null)
                return new AppResponse(publishers);
            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers/{publisherId}", e);
        }


    }


    @PATCH
    @Path("/{publisherId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchPublishers(Object request, @PathParam("publisherId") String publisherId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Publisher publisher = new Publisher(
                    publisherId,
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("username"),
                    json.getString("password"),
                    json.getString("email"),
                    json.getString("phone"),
                    json.getString("location"),
                    json.getString("type_id"),
                    json.getInt("coin")
            );

            PublisherManager.getInstance().updatePublisher(publisher);

        }catch (Exception e){
            throw handleException("PATCH publishers/{publisherId}", e);
        }

        return new AppResponse("Update Successful");
    }


    @DELETE
    @Path("/{publisherId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deletePublishers(@PathParam("publisherId") String publisherId){

        try{
            PublisherManager.getInstance().deletePublisher(publisherId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE publishers/{publisherId}", e);
        }
    }

    @POST
    @Path("/reset")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse resetPublishers(){

        try{
            PublisherManager.getInstance().reSetPublisher();
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherList();
            return new AppResponse(publishers);
        }catch (Exception e){
            throw handleException("Reset publishers", e);
        }

    }

    // http://localhost:8080/api/publishers/location/5de34728c97ba821ce644f1e
    @GET
    @Path("/location/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublisherLocation(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherById(publisherId);

            if(publishers != null) {
                String location = null;
                for (Publisher publisher : publishers)
                    location = publisher.getLocation();
                return new AppResponse(location);
            }
            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers/{publisherId}", e);
        }
    }

    // http://localhost:8080/api/publishers/weather/5de34728c97ba821ce644f1e
    @GET
    @Path("/weather/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublisherWeather(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            return new AppResponse("Sunny");
        }catch (Exception e){
            throw handleException("GET /publishers/{publisherId}", e);
        }
    }


    // http://localhost:8080/api/publishers/order/history/5de34728c97ba821ce644f1e
    @GET
    @Path("/order/history/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublisherOrderHistory(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            ArrayList<Order> orderListBefore = new ArrayList<Order>();
            orderListBefore = OrderManager.getInstance().getOrderList();
            ArrayList<Order> orderList = new ArrayList<Order>();
            System.out.println(orderListBefore.size());
            for (Order order : orderListBefore)
            {
                ArrayList<Event> eventList = EventManager.getInstance().getEventById(order.getEvent_id());
                Event event = null;
                for (Event each : eventList)
                    event = each;
                ArrayList<Publisher> publisherList = PublisherManager.getInstance().getPublisherById(event.getPublisher_id());
                Publisher publisher = null;
                for (Publisher each: publisherList)
                    publisher = each;
                if (publisher.getPublisher_id().equals(publisherId))
                    orderList.add(order);
            }

            if ((orderList != null)&&(orderList.size() > 0))
                return new AppResponse(orderList);
            else
                throw new HttpBadRequestException(0, "Problem with getting order history");
        }catch (Exception e){
            throw handleException("GET /order/history/{publisherId}", e);
        }
    }

    // http://localhost:8080/api/publishers/create/event/5de34728c97ba821ce644f1e
    @POST
    @Path("/create/event/{publisherId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postEventByPublisher(Object request, @Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Event newevent = new Event(
                    null,
                    publisherId,
                    json.getString("name"),
                    json.getString("type_id"),
                    json.getString("date"),
                    json.getString("time"),
                    json.getString("location"),
                    json.getInt("price"),
                    json.getInt("capacity"),
                    json.getInt("recommended_people"),
                    json.getString("description")
            );
            EventManager.getInstance().createEvent(newevent);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST events", e);
        }
    }

    // http://localhost:8080/api/publishers/change/event/5de49d406a22cc07e6e63226/5de4a8056a22cc08ef780eba
    @PATCH
    @Path("/change/event/{publisherId}/{eventId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postEventByPublisher(Object request, @Context HttpHeaders headers, @PathParam("publisherId") String publisherId, @PathParam("eventId") String eventId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            JSONObject json = null;

            json = new JSONObject(ow.writeValueAsString(request));
            Event event = new Event(
                    eventId,
                    publisherId,
                    json.getString("name"),
                    json.getString("type_id"),
                    json.getString("date"),
                    json.getString("time"),
                    json.getString("location"),
                    json.getInt("price"),
                    json.getInt("capacity"),
                    json.getInt("recommended_people"),
                    json.getString("description")
            );

            EventManager.getInstance().updateEvent(event);
            return new AppResponse("Patch Successful");

        }catch (Exception e){
            throw handleException("PATCH events", e);
        }
    }

    // http://localhost:8080/api/publishers/delete/event/5de49d406a22cc07e6e63226/5de4a8056a22cc08ef780eba
    @DELETE
    @Path("/delete/event/{publisherId}/{eventId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteEventByPublisher(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId, @PathParam("eventId") String eventId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            ArrayList<Event> eventList = new ArrayList<Event>();
            eventList = EventManager.getInstance().getEventById(eventId);
            if (eventList.size() == 0)
                throw new HttpBadRequestException(0, "No such user");
            EventManager.getInstance().deleteEvent(eventId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE events/{eventId}", e);
        }
    }

    // http://localhost:8080/api/publishers/change/order/5de49d406a22cc07e6e63226/5de49d406a22cc07e6e6322e
    @PATCH
    @Path("/change/order/{publisherId}/{orderId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse patchOrderByPublisher(Object request, @Context HttpHeaders headers, @PathParam("publisherId") String publisherId, @PathParam("orderId") String orderId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            ArrayList<Order> orderList = new ArrayList<Order>();
            orderList = OrderManager.getInstance().getOrderById(orderId);
            if (orderList.size() == 0)
                throw new HttpBadRequestException(0, "No such order");
            Order order = null;
            for (Order each : orderList)
                order = each;
            ArrayList<Event> eventList = EventManager.getInstance().getEventById(order.getEvent_id());
            Event event = null;
            for (Event each : eventList)
                event = each;
            ArrayList<Publisher> publisherList = PublisherManager.getInstance().getPublisherById(event.getPublisher_id());
            Publisher publisher = null;
            for (Publisher each: publisherList)
                publisher = each;
            if (publisher.getPublisher_id().equals(publisherId))
            {
                order.setStatus("Cancelled");
                OrderManager.getInstance().updateOrder(order);
                return new AppResponse("Patch Successful");
            }
            throw new HttpBadRequestException(0, "Order's publisher does not match publisher now");

        }catch (Exception e){
            throw handleException("PATCH order", e);
        }
    }

    // http://localhost:8080/api/publishers/payment/history/5de49d406a22cc07e6e63226
    @GET
    @Path("/payment/history/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublisherPaymentHistory(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            ArrayList<Payment> paymentListBefore = new ArrayList<Payment>();
            paymentListBefore = PaymentManager.getInstance().getPaymentList();
            ArrayList<Payment> paymentList = new ArrayList<Payment>();
            //System.out.println(paymentListBefore.size());
            for (Payment payment : paymentListBefore)
            {
                ArrayList<Order> orderList = OrderManager.getInstance().getOrderById(payment.getOrder_id());
                Order order = null;
                for (Order each : orderList)
                    order = each;
                ArrayList<Event> eventList = EventManager.getInstance().getEventById(order.getEvent_id());
                Event event = null;
                for (Event each : eventList)
                    event = each;
                ArrayList<Publisher> publisherList = PublisherManager.getInstance().getPublisherById(event.getPublisher_id());
                Publisher publisher = null;
                for (Publisher each: publisherList)
                    publisher = each;
                if (publisher.getPublisher_id().equals(publisherId))
                    paymentList.add(payment);
            }

            if ((paymentList != null)&&(paymentList.size() > 0))
                return new AppResponse(paymentList);
            else
                throw new HttpBadRequestException(0, "Problem with getting payment history");
        }catch (Exception e){
            throw handleException("GET /payment/history/{publisherId}", e);
        }
    }

    // http://localhost:8080/api/publishers/localtime/5de49d406a22cc07e6e63226
    @GET
    @Path("/localtime/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublisherLocalTime(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            return new AppResponse("12:13:14");
        }catch (Exception e){
            throw handleException("GET /local time", e);
        }
    }

}
