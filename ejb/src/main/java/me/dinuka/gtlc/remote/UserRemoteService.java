package me.dinuka.gtlc.remote;

import jakarta.ejb.Remote;
import me.dinuka.gtlc.dto.UserDTO;

@Remote
public interface UserRemoteService {
    String register(UserDTO userDTO);
}
