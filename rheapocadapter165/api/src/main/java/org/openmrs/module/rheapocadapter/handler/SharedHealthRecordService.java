package org.openmrs.module.rheapocadapter.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.rheapocadapter.RHEAConstants;
import org.openmrs.module.rheapocadapter.impl.HL7MessageTransformer;
import org.openmrs.module.rheapocadapter.service.MessageTransformer;
import org.openmrs.module.rheapocadapter.transaction.ArchiveTransaction;
import org.openmrs.module.rheapocadapter.transaction.ErrorTransaction;
import org.openmrs.module.rheapocadapter.transaction.ProcessingTransaction;
import org.openmrs.module.rheapocadapter.transaction.Transaction;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;

public class SharedHealthRecordService {
	private ResponseHandler response = new ResponseHandler();

	private RequestHandler requestHandler = new RequestHandler();

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @see org.openmrs.module.rheapocadapter.service.ClientRegistryService#savePatientEncounter(java.util.TreeMap)
	 */
	public synchronized String savePatientEncounter(Encounter encounter,
			String clientId) {

		String result = "";
		MessageTransformer messageTransformer = new HL7MessageTransformer();
		String message = "";
		message = (String) messageTransformer
				.encodingEncounterToMessage(encounter);
		if ("".equals(message) || message.equals(null)) {
			return "Register failed due to message creation, Contact Administrator";
		}
		log.info("Message Sent = " + message);
		// TransactionUtil.setCreator(encounter.getCreator());
		String[] methd = new String[] { "POST", "SavePatientEncounter" };
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("patientId", clientId);
		Transaction item = requestHandler.sendRequest(methd, message,
				parameters);
		if (item instanceof ArchiveTransaction) {
			item.setMessage("EncounterId=" + encounter.getEncounterId());
			String url = methd[0] + " " + item.getUrl();
			item.setUrl(url);
			result = "Save Patient Encounter succeded";
		} else if (item instanceof ProcessingTransaction) {
			item.setMessage("EncounterId=" + encounter.getEncounterId() + "");
			result = "Save Patient Encounter failed, try again later";
		} else if (item instanceof ErrorTransaction) {

			item.setMessage("EncounterId=" + encounter.getEncounterId() + "");
			result = "Save Patient Encounter failed, Contact Administrator";

		}
		response.handleResponse(item);
		return result;

	}

	/**
	 * @see org.openmrs.module.rheapocadapter.service.ClientRegistryService#savePatientEncounter(java.util.TreeMap)
	 */
	public synchronized String saveMultiplePatientEncounter(Patient patient,
			List<Encounter> encounters, String clientId) {

		String result = "";
		MessageTransformer messageTransformer = new HL7MessageTransformer();
		String message = "";
		message = (String) messageTransformer.encodingEncounterToMessage(
				patient, encounters);
		if ("".equals(message) || message.equals(null)) {
			return "Register failed due to message creation, Contact Administrator";
		}
		log.info("Message Sent = " + message);
		// TransactionUtil.setCreator(encounter.getCreator());
		String[] methd = new String[] { "POST", "SavePatientEncounter" };
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("patientId", clientId);
		Transaction item = requestHandler.sendRequest(methd, message,
				parameters);
		String encounterIds = "";
		for (Encounter e : encounters) {
			if (encounterIds != "")
				encounterIds += ", ";
			encounterIds += e.getEncounterId();
		}
		if (item instanceof ArchiveTransaction) {
			item.setMessage("Encounter with Id " + encounterIds + " Sent");
			String url = methd[0] + " " + item.getUrl();
			item.setUrl(url);
			result = "Save Patient Encounter succeded";
		} else if (item instanceof ProcessingTransaction) {
			item.setMessage("EncounterId=" + encounterIds + "");
			result = "Save Patient Encounter failed, try again later";
		} else if (item instanceof ErrorTransaction) {
			item.setMessage("EncounterId = " + encounterIds + "");
			result = "Save Patient Encounter failed, Contact Administrator";

		}
		response.handleResponse(item);
		return result;

	}

	/**
	 * @see org.openmrs.module.rheapocadapter.service.ClientRegistryService#getPatientClinicalDataFromSHR(java.util.TreeMap)
	 */
	public synchronized String getPatientClinicalDataFromSHR(Patient patient) {
		String implementationId = "";
		String result = "";
		try {

			try {
				implementationId = (Context.getAdministrationService()
						.getImplementationId().getImplementationId() != null) ? Context
						.getAdministrationService().getImplementationId()
						.getImplementationId()
						: "rwanda000";
			} catch (NullPointerException e) {
				log.error("No Implementation Id  set;");
				implementationId = "rwanda000";
			}
			String clientId = "";
			String fosaid = implementationId.substring(implementationId
					.indexOf("rwanda") + 6);
			PatientIdentifierType nid = Context.getPatientService()
					.getPatientIdentifierTypeByName("NID");
//			PatientIdentifierType mutuelle = Context.getPatientService()
//					.getPatientIdentifierTypeByName("Mutuelle");
//			PatientIdentifierType rama = Context.getPatientService()
//					.getPatientIdentifierTypeByName("RAMA");
			PatientIdentifierType primaryCare = Context.getPatientService()
					.getPatientIdentifierTypeByName("Primary Care ID Type");

			if (getPatientIdentifierByIdentifierType(patient, nid) != null) {
				clientId = nid + "-"
						+ getPatientIdentifierByIdentifierType(patient, nid);
//			} 
//			else if (getPatientIdentifierByIdentifierType(patient, rama) != null) {
//				clientId = rama + "-"
//						+ getPatientIdentifierByIdentifierType(patient, rama);
//			} else if (getPatientIdentifierByIdentifierType(patient, mutuelle) != null) {
//				clientId = mutuelle
//						+ "-"
//						+ getPatientIdentifierByIdentifierType(patient,
//								mutuelle);
			} else if (getPatientIdentifierByIdentifierType(patient,
					primaryCare) != null) {
				clientId = "OMRS"
						+ fosaid
						+ "-"
						+ getPatientIdentifierByIdentifierType(patient,
								primaryCare);
			}
			if (clientId == "") {

				clientId = patient.getPatientIdentifier().getIdentifierType()
						+ "-" + patient.getPatientIdentifier().getIdentifier();
			}
			log.info(clientId + " Client Id");
			String relatedEncounters = "";

			try {
				EncounterService encService = Context.getEncounterService();
				String[] methd = new String[] { "GET", "GetClinicalData" };
				TreeMap<String, String> parameters = new TreeMap<String, String>();
				parameters.put("patientId", clientId);
				String createdSince = getLastDateEncounterReceived(patient);
				if (createdSince != "" && createdSince != "null"
						&& createdSince != null) {
					log.info("createdSince " + createdSince);
					parameters.put("createdInSince", createdSince);
				}
				Transaction item = requestHandler.sendRequest(methd, result,
						parameters);
				if (item instanceof ArchiveTransaction) {
					String url = methd[0] + " " + item.getUrl();
					item.setUrl(url);
					result = item.getMessage();

					HL7MessageTransformer messageTransformer = new HL7MessageTransformer();
					Message message = (Message) messageTransformer
							.translateMessage(result);

					List<Encounter> encounterToSave = messageTransformer
							.messageToEncounter((ORU_R01) message);
					log.info(encounterToSave.size() + " encounter to save");
					for (Encounter e : encounterToSave) {
						EncounterType type = e.getEncounterType();
						List<EncounterType> encTypes = encService
								.getAllEncounterTypes();
						log.info(encService.getEncounterType(type.getName()
								+ " IMPORTED"));
						boolean encounterTypeExist = false;
						for (EncounterType ty : encTypes) {
							if (ty.getName().equalsIgnoreCase(
									type.getName() + " IMPORTED")) {
								encounterTypeExist = true;
							}
						}
						if (!encounterTypeExist) {
							EncounterType newType = new EncounterType();
							newType.setCreator(Context.getAuthenticatedUser());
							newType.setDescription(type.getName() + " IMPORTED");
							newType.setName(type.getName() + " IMPORTED");
							encService.saveEncounterType(newType);

						}
						e.setEncounterType(encService.getEncounterType(type
								.getName() + " IMPORTED"));
						Encounter saved = encService.saveEncounter(e);
						if (relatedEncounters.equals(""))
							relatedEncounters += saved.getId();
						else
							relatedEncounters += "," + saved.getId();

					}
					ArchiveTransaction transaction = (ArchiveTransaction) item;
					transaction.setRelatedEncounter(relatedEncounters);
					response.handleResponse(transaction);
					result = "Result after saving encounter "
							+ relatedEncounters + " "
							+ transaction.getMessage();
				} else if (item instanceof ProcessingTransaction) {
					result = "Get Clinical Data failed for " + clientId
							+ ", try again later";
					item.setMessage(result);
					response.handleResponse(item);
				} else if (item instanceof ErrorTransaction) {
					result = "Get Clinical Data failed for " + clientId
							+ ", Contact Administrator";
					item.setMessage(result);
					response.handleResponse(item);
				}

			} catch (HL7Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	private PatientIdentifier getPatientIdentifierByIdentifierType(
			Patient patient, PatientIdentifierType idType) {
		return ((patient.getPatientIdentifier(idType) != null) && (patient
				.getPatientIdentifier(idType).getIdentifierType()
				.equals(idType))) ? (patient.getPatientIdentifier(idType))
				: null;

	}

	private synchronized String getLastDateEncounterReceived(Patient patient) {

		// String[] allowedEncounterType = Context.getAdministrationService()
		// .getGlobalProperty(RHEAConstants.ENCOUNTER_TYPE).split(",");
		// ArrayList<EncounterType> encounterTypes = new
		// ArrayList<EncounterType>();
		// for (int i = 0; i < allowedEncounterType.length; i++) {
		// allowedEncounterType[i] = allowedEncounterType[i].trim();
		// EncounterType type = Context.getEncounterService()
		// .getEncounterType(allowedEncounterType[i] + " IMPORTED");
		// if (type != null) {
		// encounterTypes.add(type);
		// }
		// }

		List<Encounter> enc = Context.getEncounterService().getEncounters(
				patient, null, null, null, null, null, null, false);

		ArrayList<Date> encounterDates = new ArrayList<Date>();
		if (enc.size() > 0) {
			for (Encounter e : enc) {
				if (e.getEncounterType().getName().endsWith("IMPORTED")) {
					encounterDates.add(e.getDateCreated());
				}

			}

			long max = Long.MIN_VALUE;
			Date maxDate = null;
			if (encounterDates.size() > 0) {
				for (Date value : encounterDates) {
					long v = value.getTime();
					if (v > max) {
						max = v;
						maxDate = value;
					}
				}

				String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
				SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
				String formattedDate = formatter.format(maxDate);
				return (maxDate != null) ? formattedDate : "";
			}
		} else {
			return "";
		}

		return "";
	}
}
