package io.reinno.auth.server.ws;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
public class VersionController {

	@GetMapping
	public String get() {

		return getClass().getPackage().getImplementationVersion();
	}
}
