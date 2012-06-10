package com.pizza.ui.url_encryption;


/**
 * Utility that converts readable URL parameter names for entity id's that should be encrypted
 * within the UI to an encoded parameter name and vice-versa.
 *
 */
public final class UrlParms {
//
//	private static final Map<String, String> encodeMap = new HashMap<String, String>();
//
//	private static final Map<String, String> decodeMap = new HashMap<String, String>();
//
//	static {
//		for (final UrlParm id : UrlParm.values()) {
//			if (encodeMap.containsKey(id.getDecodedName())) {
//				throw new InvalidEntityException("Duplicate id in UrlParm enum.");
//			}
//			encodeMap.put(id.getDecodedName(), id.toString());
//		}
//		for (final String key : encodeMap.keySet()) {
//			final String value = encodeMap.get(key);
//			if (decodeMap.containsKey(value)) {
//				throw new InvalidEntityException("Duplicate encoded id in UrlParm enum.");
//			}
//			decodeMap.put(value, key);
//		}
//	}
//
//	private UrlParms() {
//		// static only
//	}
//
//	/**
//	 * Returns the encoded parameter name for the given readable parameter name.
//	 *
//	 * @param name the readable (decoded) parameter name from the UrlParm enum
//	 * @return the encoded parameter name, or null if the parameter name has no encoded value
//	 */
//	public static String getEncodedName(final String name) {
//		return encodeMap.get(name);
//	}
//
//	/**
//	 * Returns the (decoded) readable parameter name for the given encoded parameter name.
//	 *
//	 * @param name the encoded parameter name
//	 * @return the (decoded) readable parameter name from the UrlParm enum, or null if the
//	 *         parameter name has no decoded value
//	 */
//	public static String getDecodedName(final String name) {
//		return decodeMap.get(name);
//	}
//
//	/**
//	 * returns whether the url parm is a long or not.
//	 * @param decodedName the name of the parm, decoded
//	 * @return true if long, false if not.
//	 */
//	public static boolean isLong(final String decodedName) {
//		return UrlParm.valueOf(decodedName).isLong();
//	}
//	/**
//	 * Constants for entity id's and their encoded parameter names for use in URL's.
//	 *
//	 * @author Ameer Antar
//	 */
//	public static enum UrlParm {
//		/**
//		 * accountId.
//		 */
//		accountId("_ac"),
//		/**
//		 * addtlReadingId.
//		 */
//		addtlReadingId("_ar"),
//		/**
//		 * additionalReadingId.
//		 */
//		additionalReadingId("_addr"),
//		/**
//		 * adminId.
//		 */
//		adminId("_a"),
//		/**
//		 * agendaItemId.
//		 */
//		agendaItemId("_ai"),
//		/**
//		 * adminIntakeId.
//		 */
//		adminIntakeId("_ain"),
//		/**
//		 * affiliatedHospitalId.
//		 */
//		affiliatedHospitalId("_ah"),
//		/**
//		 * affiliatedHospitalIds.
//		 */
//		affiliatedHospitalIds("_ahs"),
//		/**
//		 * assessmentId.
//		 */
//		assessmentId("_as"),
//		/**
//		 * assessmentIds.
//		 */
//		assessmentIds("_ass"),
//		/**
//		 * The ID for an AssistantFollowUpEntry.
//		 */
//		assistantFollowUpEntryId("_afue"),
//		/**
//		 * assistantId.
//		 */
//		assistantId("_assis"),
//		/**
//		 * attachmentId.
//		 */
//		attachmentId("_at"),
//		/**
//		 * attachmentId.
//		 */
//		attachmentIds("_ats"),
//		/**
//		 * bulkImportId.
//		 */
//		bulkImportId("_bi"),
//		/**
//		 * cellPhoneCarrierId.
//		 */
//		cellPhoneCarrierId("_cpc"),
//		/**
//		 * codeSystemId.
//		 */
//		codeSystemId("_cs"),
//		/**
//		 * conditionId.
//		 */
//		conditionId("_c"),
//		/**
//		 * DecisionSupportContentProvider.id.
//		 */
//		contentProviderId("_dp"),
//
//		/**
//		 * currentSelectionId.
//		 */
//		currentSelectionId("_csl"),
//		/**
//		 * currentTemplateId.
//		 */
//		currentTemplateId("_ct"),
//		/**
//		 * declined provider id.
//		 */
//		declinedProviderId("_dpi"),
//		/**
//		 * disclaimerId.
//		 */
//		disclaimerId("_d"),
//		/**
//		 * disclaimerIds.
//		 */
//		disclaimerIds("_ds"),
//		/**
//		 * dosageId.
//		 */
//		dosageId("_do"),
//		/**
//		 * downtimeId.
//		 */
//		downtimeId("_dw"),
//		/**
//		 * durationId.
//		 */
//		durationId("_du"),
//		/**
//		 * engagementId.
//		 */
//		engagementId("_e"),
//		/**
//		 * engagementIds.
//		 */
//		engagementIds("_es"),
//		/**
//		 * engagementRxId.
//		 */
//		engagementRxId("_erx"),
//		/**
//		 * engTemplateId.
//		 */
//		engTemplateId("_et"),
//		/**
//		 * entryId.
//		 */
//		entryId("_ent"),
//		/**
//		 * The authentication type, e.g. SSO, DIRECT, or ENROLL
//		 */
//		entryPoint("_ep"),
//		/**
//		 * etDateAttributeId.
//		 */
//		etDateAttributeId("_etd"),
//		/**
//		 * externalRuleId.
//		 */
//		externalRuleId("_exr"),
//		/**
//		 * frequencyId.
//		 */
//		frequencyId("_fr"),
//		/**
//		 * gotoPageId.
//		 */
//		gotoPageId("_gp"),
//		/**
//		 * healthDirectiveId.
//		 */
//		healthDirectiveId("_hd"),
//		/**
//		 * healthPlanId.
//		 */
//		healthPlanId("_hp"),
//		/**
//		 * healthRefId.
//		 */
//		healthRefId("_hr"),
//		/**
//		 * imageId.
//		 */
//		imageId("_i"),
//		/**
//		 * practiceFollowUpId.
//		 */
//		practiceFollowUpId("_pf"),
//		/**
//		 * proxyMemberId.
//		 */
//		proxyMemberId("_prm"),
//		/**
//		 * proxyProviderId.
//		 */
//		proxyProviderId("_prp"),
//		/**
//		 * importId.
//		 */
//		importId("_imi"),
//		/**
//		 * inNetContact.
//		 */
//		inNetContact("_inc"),
//		/**
//		 * intakeId.
//		 */
//		intakeId("_ii"),
//		/**
//		 * intakeImageId.
//		 */
//		intakeImageId("_im"),
//		/**
//		 * intakeTemplateId.
//		 */
//		intakeTemplateId("_it"),
//		/**
//		 * mediaObjectId.
//		 */
//		intakeItemId("_it"),
//		/**
//		 * ltDateAttributeId.
//		 */
//		ltDateAttributeId("_lt"),
//		/**
//		 * legalResidencyId.
//		 */
//		legalResidencyId("_lr"),
//		/**
//		 * mediaObjectId.
//		 */
//		mediaObjectId("_mo"),
//		/**
//		 * mediaId.
//		 */
//		mediaId("_mi"),
//		/**
//		 * medicalHomeId.
//		 */
//		medicalHomeId("_mh"),
//		/**
//		 * medicalHomeManagerId.
//		 */
//		medicalHomeManagerId("_mhm"),
//		/**
//		 * memberId.
//		 */
//		memberId("_m"),
//		/**
//		 * notificationId.
//		 */
//		notificationId("_n"),
//		/**
//		 * otherAffiliation.
//		 */
//		otherAffiliationId("_oa"),
//		/**
//		 * otherAffiliations.
//		 */
//		otherAffiliationIds("_oas"),
//		/**
//		 * pageConditionId.
//		 */
//		pageConditionId("_pc"),
//		/**
//		 * pageId.
//		 */
//		pageId("_pg"),
//		/**
//		 * pageItemId.
//		 */
//		pageItemId("_pi"),
//		/**
//		 * pageTextId.
//		 */
//		pageTextId("_pt"),
//		/**
//		 * paired device id - as in iphone/ipad.
//		 */
//		pairedDeviceId("_pd"),
//		/**
//		 * pharmacyId.
//		 */
//		pharmacyId("_ph"),
//
//		/**
//		 * The {@code PostVisitFollowUpItem} ID.
//		 */
//		postVisitFollowUpItemId("_pvi"),
//
//		/**
//		 * primaryProviderId.
//		 */
//		primaryProviderId("_pp"),
//		/**
//		 * profileAttributeId.
//		 */
//		profileAttributeId("_paa"),
//		/**
//		 * profileAttrId.
//		 */
//		profileAttrId("_pa"),
//		/**
//		 * profileUpdateConditionId.
//		 */
//		profileUpdateConditionId("_puc"),
//
//		/**
//		 * providerToCoverId.
//		 */
//		providerToCoverId("_ptc"),
//
//		/**
//		 * providerId.
//		 */
//		providerId("_p"),
//		/**
//		 * providerIds.
//		 */
//		providerIds("_ps"),
//		/**
//		 * providerReferenceId.
//		 */
//		providerReferenceId("_pr"),
//		/**
//		 * questionConditionId.
//		 */
//		questionConditionId("_qc"),
//		/**
//		 * questionId.
//		 */
//		questionId("_q"),
//		/**
//		 * recipientIds.
//		 */
//		recipientIds("_rid"),
//		/**
//		 * recordId.
//		 */
//		recordId("_r"),
//		/**
//		 * reportRuleId.
//		 */
//		reportRuleId("_rr"),
//		/**
//		 * reportConditionId.
//		 */
//		reportConditionId("_rc"),
//		/**
//		 * secureInboxMessageId.
//		 */
//		secureInboxMessageId("_sim"),
//		/**
//		 * secureMessageId.
//		 */
//		secureMessageId("_sm"),
//		/**
//		 * secureMessageThreadId.
//		 */
//		secureMessageThreadId("_smt"),
//		/**
//		 * secureMessageValuationId.
//		 */
//		secureMessageValuationId("secureMessageValuationId"),
//		/**
//		 * selectedTemplateIds.
//		 */
//		selectedTemplateIds("_sts"),
//		/**
//		 * sendOnBehalfOfId.
//		 */
//		sendOnBehalfOfId("_sobouid"),
//		/**
//		 * separatorId.
//		 */
//		separatorId("_s"),
//		/**
//		 * secureMessageAttachmentId.
//		 */
//		smAttachmentId("_sma"),
//		/**
//		 * stateId.
//		 */
//		stateId("_st"),
//		/**
//		 * statementSourceId.
//		 */
//		statementSourceId("_ss"),
//		/**
//		 * substanceId.
//		 */
//		substanceId("_sub"),
//		/**
//		 * templateId.
//		 */
//		templateId("_te"),
//		/**
//		 * termId.
//		 */
//		termId("_tm"),
//		/**
//		 * templateId.
//		 */
//		topicId("_to"),
//		/**
//		 * trackerAttrId.
//		 */
//		trackerAttrId("_ta"),
//		/**
//		 * trackerId.
//		 */
//		trackerId("_tr"),
//		/**
//		 * TrackerRecord.id.
//		 */
//		trackerRecordIds("_trr"),
//		/**
//		 * TrackerTemplate.id.
//		 */
//		trackerTemplateId("_tt"),
//		/**
//		 * TransferredFromEngagementId.
//		 */
//		transferredFromEngagementId("_tfe"),
//		/**
//		 * TransferToProviderId.
//		 */
//		transferToProviderId("_ttp"),
//		/**
//		 * triageId.
//		 */
//		triageId("_trg"),
//		/**
//		 * updateAttributeId.
//		 */
//		updateAttributeId("_ua"),
//		/**
//		 * versionOfId.
//		 */
//		versionOfId("_vo"),
//
//		/**
//		 * practiceId.
//		 */
//		practiceId("_prac"),
//
//		/**
//		 * Bank Account id.
//		 */
//		bankAccountId("_bA"),
//
//		/**
//		 * The Waiting Address Entry ID.
//		 */
//		waitingAddressEntryId("_wrE"),
//
//		/**
//		 * The Waiting Address ID.
//		 */
//		waitingAddressId("_wr"),
//
//		/**
//		 * ID of a provider being covered.
//		 */
//		coveredProviderId("_covdP"),
//
//		/**
//		 * Account overrides as JSON.
//		 */
//		shape("_accJ", false);
//
//		private String encodedParameter;
//		private boolean isLong;
//
//		private UrlParm(final String encodedParameter) {
//			this(encodedParameter, true);
//		}
//
//		private UrlParm(final String encodedParameter, final boolean isLong) {
//			this.encodedParameter = encodedParameter;
//			this.isLong = isLong;
//		}
//
//		/**
//		 * Returns the encoded parameter name.
//		 * {@inheritDoc}
//		 *
//		 * @see java.lang.Enum#toString()
//		 */
//		@Override
//		public String toString() {
//			return getEncodedName();
//		}
//
//		/**
//		 * Get the encoded name for this Url Parameter.
//		 * @return encoded name.
//		 */
//		public String getEncodedName() {
//			if (!EncryptUtil.ENABLE_ENCODE) {
//				return getDecodedName();
//			}
//			else {
//				return encodedParameter;
//			}
//		}
//
//		/**
//		 * Returns the decoded name of the UrlParm.
//		 * @return the decoded name of the UrlParm
//		 */
//		public String getDecodedName() {
//			return super.toString();
//		}
//
//		public boolean isLong() {
//			return isLong;
//		}
//
//	}

}
