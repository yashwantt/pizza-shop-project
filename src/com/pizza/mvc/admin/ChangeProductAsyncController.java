package com.pizza.mvc.admin;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaOrder.StatusName;
import com.pizza.service.AdminService;
import com.pizza.ui.controller.ControllerUtils;
import com.pizza.util.StringUtils;


public class ChangeProductAsyncController extends MultiActionController {
	private AdminService adminService;

	
	public void initialize_db(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		
		ServletContext context = httpServletRequest.getSession().getServletContext();
		String realContextPath = context.getRealPath(httpServletRequest.getContextPath());
		
		adminService.initializeDb(realContextPath);
		adminService.insertDemoOrders(realContextPath);

		ControllerUtils.writeSuccess(httpServletResponse);
	}
	
	
	
	public void activatePizzaSize(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		
		Long id = Long.parseLong((String)httpServletRequest.getParameter("pizzaSizeId"));
		adminService.updatePizzaSizeActiveFlag(id, true);
		ControllerUtils.writeSuccess(httpServletResponse);
	}
	
	
	public void deactivatePizzaSize(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		
		Long id = Long.parseLong((String)httpServletRequest.getParameter("pizzaSizeId"));
		adminService.updatePizzaSizeActiveFlag(id, false);
		ControllerUtils.writeSuccess(httpServletResponse);
	}
	
	public void activatePizzaTopping(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		
		Long id = Long.parseLong((String)httpServletRequest.getParameter("pizzaToppingId"));
		adminService.updatePizzaToppingActiveFlag(id, true);
		ControllerUtils.writeSuccess(httpServletResponse);
	}

	public void deactivatePizzaTopping(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		
		Long id = Long.parseLong((String)httpServletRequest.getParameter("pizzaToppingId"));
		adminService.updatePizzaToppingActiveFlag(id, false);
		ControllerUtils.writeSuccess(httpServletResponse);
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
