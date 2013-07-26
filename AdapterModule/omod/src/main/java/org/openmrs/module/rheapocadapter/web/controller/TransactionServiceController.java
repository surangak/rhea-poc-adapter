/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.rheapocadapter.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.module.rheapocadapter.handler.EnteredHandler;
import org.openmrs.module.rheapocadapter.transaction.ArchiveTransaction;
import org.openmrs.module.rheapocadapter.transaction.ErrorTransaction;
import org.openmrs.module.rheapocadapter.transaction.ProcessingTransaction;
import org.openmrs.module.rheapocadapter.transaction.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ;
 */
@Controller
@SuppressWarnings("unchecked")
public class TransactionServiceController {

	protected static final Log log = LogFactory
			.getLog(TransactionServiceController.class);

	@RequestMapping("/module/rheapocadapter/manageQueue.form")
	public String listAllQueues(ModelMap map) {
		EnteredHandler enteredHandler = new EnteredHandler();
		List<ArchiveTransaction> archiveTransactions = new ArrayList<ArchiveTransaction>();
		archiveTransactions = (List<ArchiveTransaction>) enteredHandler
				.getArchiveQueue();
		Collections.reverse(archiveTransactions);
		map.addAttribute("archiveTransactions", archiveTransactions);
		log.info("Archive Transaction Size " + archiveTransactions.size());
		return "/module/rheapocadapter/manageQueue";
	}

	@RequestMapping("/module/rheapocadapter/processingQueue.form")
	public String showProcessingQueue(ModelMap map) {
		EnteredHandler enteredHandler = new EnteredHandler();
		List<ProcessingTransaction> processingTransactions = new ArrayList<ProcessingTransaction>();
		processingTransactions = (List<ProcessingTransaction>) enteredHandler
				.getProcessingQueue();
		Collections.reverse(processingTransactions);
		map.addAttribute("processingTransactions", processingTransactions);

		return "/module/rheapocadapter/processingQueue";

	}

	@RequestMapping("/module/rheapocadapter/errorQueue.form")
	public String showErrorQueue(ModelMap map) {
		EnteredHandler enteredHandler = new EnteredHandler();
		List<ErrorTransaction> errorTransactions = (List<ErrorTransaction>) enteredHandler
				.getErrorQueue();
		Collections.reverse(errorTransactions);
		map.addAttribute("errorTransactions", errorTransactions);

		return "/module/rheapocadapter/errorQueue";
	}

	@RequestMapping("/module/rheapocadapter/backEnteredQueue.form")
	public String showBackEnteredQueue(ModelMap map) {
		EnteredHandler enteredHandler = new EnteredHandler();
		List<Encounter> encounter = enteredHandler.getEncounterNotSent();
		Collections.reverse(encounter);
		map.addAttribute("encounterNotSent", encounter);

		return "/module/rheapocadapter/backEnteredQueue";
	}

	@RequestMapping("/module/rheapocadapter/sentBackEnteredData.form")
	public String sendBackEnteredQueue(ModelMap map) {
		EnteredHandler enteredHandler = new EnteredHandler();
		log.info("Task Started for sending BackEnteredData");
		enteredHandler.sendBackEntered();

		return "/module/rheapocadapter/backEnteredQueue";
	}
	@RequestMapping("/module/rheapocadapter/filterData.form")
	public String filterDates(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws ParseException{
		
		String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String qType =  request.getParameter("qType");
        
        map.addAttribute("dateFrom", dateFrom);
        map.addAttribute("dateTo", dateTo);
        EnteredHandler enteredHandler = new EnteredHandler();
        
        
        
        if(qType.equals("Archive")){
        	List <Transaction> archiveTransactions;
    		List<Transaction> archivedTransactions = new ArrayList<Transaction>();
    		
    		if(dateFrom.equals("") && dateTo.equals("")){
        		archivedTransactions = (List<Transaction>) enteredHandler.getArchiveQueue();
        		map.addAttribute("archiveTransactions", archivedTransactions);
    		}
    		else{
    			archivedTransactions = (List<Transaction>) enteredHandler.getArchiveQueue();
    			archiveTransactions = dateFilter(archivedTransactions, dateFrom, dateTo);
        		Collections.reverse(archiveTransactions);
    			map.addAttribute("archiveTransactions", archiveTransactions);
    		}
    		
    		
    		
    		return "/module/rheapocadapter/manageQueue";
        }
        else if(qType.equals("Processing")){
        	List<Transaction> processingTransactions;
    		List<Transaction> processedTransactions = new ArrayList<Transaction>();
    		
    		if(dateFrom.equals("") && dateTo.equals("")){ 
    			processedTransactions = (List<Transaction>) enteredHandler
        				.getProcessingQueue();
    		}
    		else{
    			processedTransactions = (List<Transaction>) enteredHandler
        				.getProcessingQueue();
    			processingTransactions = dateFilter(processedTransactions, dateFrom, dateTo);
    			Collections.reverse(processingTransactions);
    			map.addAttribute("processingTransactions", processingTransactions);
    		}
    		
    		
        	
        	return "/module/rheapocadapter/processingQueue";
        }
        else if(qType.equals("Error")){
        	
        	List<Transaction> errorTransactions;
    		List<Transaction> erredTransactions = new ArrayList<Transaction>();
    		
    		if(dateFrom.equals("") && dateTo.equals("")){ 
    			erredTransactions = (List<Transaction>) enteredHandler
        				.getErrorQueue();
    		}
    		else{
    			erredTransactions = (List<Transaction>) enteredHandler
        				.getErrorQueue();
    			errorTransactions = dateFilter(erredTransactions, dateFrom, dateTo);
    			Collections.reverse(errorTransactions);
    			map.addAttribute("errorTransactions", errorTransactions);
    		}
        	
        	return "/module/rheapocadapter/errorQueue";
        	
        }
        else{
        	
        	//List<Transaction> errorTransactions;
    		//List<Transaction> erredTransactions = new ArrayList<Transaction>();
    		
    		List<Encounter> encounterz = new ArrayList<Encounter>();
    		
    		//List<Encounter> encounter = enteredHandler.getEncounterNotSent();
    		
    		if(dateFrom.equals("") && dateTo.equals("")){ 
    			encounterz = enteredHandler.getEncounterNotSent();
    		}
    		else{
    			encounterz = enteredHandler.getEncounterNotSent();
    			encounterz = encounterDateFilter(encounterz, dateFrom, dateTo);
    			Collections.reverse(encounterz);
    			map.addAttribute("encounterNotSent", encounterz);
    		}
        	
        	return "/module/rheapocadapter/backEnteredQueue";
        }
        
		
	
	}
	private List<Transaction> dateFilter(List<Transaction> transactions, String dateFrom, String dateTo) throws ParseException{
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if (!dateFrom.trim().isEmpty() && dateTo.trim().isEmpty()) {
			Iterator<Transaction> iterator = transactions.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getTimeRequestSent().after(df.parse(dateFrom))) {

				} else {
					iterator.remove();
				}
			}

		} else if (dateFrom.trim().isEmpty() && !dateTo.trim().isEmpty()) {
			Iterator<Transaction> iterator = transactions.iterator();
			Date dt = increaseDate(df.parse(dateTo));
			while (iterator.hasNext()) {
				if (iterator.next().getTimeRequestSent().before(dt)) {

				} else {
					iterator.remove();
				}
			}

		} else if (!dateFrom.trim().isEmpty() && !dateTo.trim().isEmpty()) {
			Iterator<Transaction> iterator = transactions.iterator();
			Date dt = increaseDate(df.parse(dateTo));
			while (iterator.hasNext()) {
				Date date = iterator.next().getTimeRequestSent();
				if (date.after(df.parse(dateFrom)) && date.before(dt)) {
                       
				} else {
					iterator.remove();
				}
			}

		}
		return transactions;
		
		
	}
private List<Encounter> encounterDateFilter(List<Encounter> encounterz, String dateFrom, String dateTo) throws ParseException{
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if (!dateFrom.trim().isEmpty() && dateTo.trim().isEmpty()) {
			Iterator<Encounter> iterator = encounterz.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getEncounterDatetime().after(df.parse(dateFrom))) {

				} else {
					iterator.remove();
				}
			}

		} else if (dateFrom.trim().isEmpty() && !dateTo.trim().isEmpty()) {
			Iterator<Encounter> iterator = encounterz.iterator();
			Date dt = increaseDate(df.parse(dateTo));
			while (iterator.hasNext()) {
				if (iterator.next().getEncounterDatetime().before(dt)) {

				} else {
					iterator.remove();
				}
			}

		} else if (!dateFrom.trim().isEmpty() && !dateTo.trim().isEmpty()) {
			Iterator<Encounter> iterator = encounterz.iterator();
			Date dt = increaseDate(df.parse(dateTo));
			while (iterator.hasNext()) {
				Date date = iterator.next().getEncounterDatetime();
				if (date.after(df.parse(dateFrom)) && date.before(dt)) {
                       
				} else {
					iterator.remove();
				}
			}

		}
		return encounterz;
		
		
	}
	private Date increaseDate(Date date){
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1); //minus number would decrement the days
        return cal.getTime();
	}
}
