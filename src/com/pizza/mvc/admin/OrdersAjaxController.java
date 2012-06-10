package com.pizza.mvc.admin;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaOrder.StatusName;
import com.pizza.service.AdminService;
import com.pizza.util.StringUtils;


public class OrdersAjaxController extends MultiActionController {
	private AdminService adminService;

	public ModelAndView getAllOrders(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		

		Set<PizzaOrder> allOrders = adminService.getOrders();
		
		JSONObject ordersJson = new JSONObject();
		ordersJson.element("identifier", "id");

		JSONArray ordersJsonArray = new JSONArray();
		for(PizzaOrder order: allOrders) {
			JSONObject orderJson = new JSONObject()
									.element("id", order.getId())
									.element("address", order.getAddress().toUIString())
									.element("status", order.getStatus())
									.element("size", StringUtils.makeProper(order.getSize().getName()))
									.element("toppings", order.getToppingsString());
			ordersJsonArray = ordersJsonArray.element(orderJson);
		}		
		
		ordersJson.element("items", ordersJsonArray);
		
		httpServletResponse.setContentType("text/plain;charset=utf-8");
		
		String cnt = ordersJson.toString();
		httpServletResponse.getWriter().write(cnt);
		httpServletResponse.getWriter().flush();
		return null;
	}
	
	

	
	public void changeOrderStatus(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Long id = Long.parseLong((String)httpServletRequest.getParameter("id"));
		String newStatusStr = (String)httpServletRequest.getParameter("status");
		StatusName newStatus = (StatusName) Enum.valueOf(StatusName.class, newStatusStr);
		
		adminService.setOrderStatus(id, newStatus);
	}
	
//	public MarkOrderReadyController() {
//		setCommandName("orderIdCommand");
//		setCommandClass(LongCommand.class);
//		setFormView("order_report");
////		setSuccessView("order_report");//TODO
//		setSuccessView("redirect:change_product_result.htm");//TODO
//	}
	
//	@Override
//	protected Object formBackingObject(HttpServletRequest request) throws Exception {
////		ProductCommand command = new ProductCommand();
////		return command;
//		ProductCommand productCommand = (ProductCommand) super.formBackingObject(request);
//		return productCommand;
//	}
	
	// this method provides fresh data (model vars) for the form on each time it
	// is displayed
//	@SuppressWarnings("rawtypes") 
//	@Override
//	protected Map referenceData(HttpServletRequest request, Object o,
//			Errors errors)  {
//		
//		Set<PizzaOrder> allOrders = adminService.getOrders();
//		
//		JSONObject ordersJson = new JSONObject();
//		ordersJson.element("identifier", "id");
//		ordersJson.element("label", "day");
//
//		JSONArray ordersJsonArray = new JSONArray();
//		for(PizzaOrder order: allOrders) {
//			JSONObject orderJson = new JSONObject()
//									.element("id", order.getId())
//									.element("day", order.getDay())
//									.element("addressNumber", order.getAddressNumber())
//									.element("statusString", order.getStatusString())
//									.element("toppings", order.getToppingsString());
//			ordersJsonArray = ordersJsonArray.element(orderJson);
//		}		
//		
//		ordersJson.element("items", ordersJsonArray);
//		
//		Map<String, Object> m = new HashMap<String, Object>();
//		m.put("allOrders", ordersJson);
//		return m;
//	}
//
//	@Override
//	protected ModelAndView onSubmit(HttpServletRequest httpServletRequest,
//			HttpServletResponse httpServletResponse, Object command,
//			BindException errors) throws Exception {
//
//		Long orderId = ((LongCommand) command).getId();
//		
//		adminService.markOrderReady(orderId);
//		
//		
//			
//		// set session var to survive redirect to success view (causes new
//		// request from browser)
////		session.setAttribute("pizzaOrder", order);
////		return new ModelAndView(new RedirectView(getSuccessView()));
//		return new ModelAndView(getSuccessView());
//	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
}
