package com.hnguyen387.jpa_persistence.ch03;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Item {
	private String name;
	private Set<Bid> bids = new HashSet<>();
	private Date auctionEnd;

	public Set<Bid> getBids() {
		return Collections.unmodifiableSet(bids);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getAuctionEnd() {
		return auctionEnd;
	}

	public void setAuctionEnd(Date auctionEnd) {
		this.auctionEnd = auctionEnd;
	}

	public void addBid(Bid bid) {
		if (bid == null)
			throw new NullPointerException("Can't not add a null bid");
		if (bid.getItem() != null)
			throw new IllegalStateException("Bid is already assigned to an Item");
		this.bids.add(bid);
		bid.setItem(this);
	}
	
}
