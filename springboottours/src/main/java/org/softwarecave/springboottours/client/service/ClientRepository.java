package org.softwarecave.springboottours.client.service;

import org.softwarecave.springboottours.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
