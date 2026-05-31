package ecom.commerce.user.service;

import ecom.commerce.user.dto.AddressDto;
import ecom.commerce.user.dto.UserRequest;
import ecom.commerce.user.dto.UserResponse;
import ecom.commerce.user.model.Address;
import ecom.commerce.user.model.User;
import ecom.commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//   private final List<User> userList = new ArrayList<>();
//    private Long id = 1L;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::maptoUserResponse)
                .collect(Collectors.toList());
    }

    public void addUsers(UserRequest userRequest) {
//        user.setId(id);
//        id++;
        User user = new User();
        updateUserFromRequest(user,userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> fetchUser(String id) {
//       return userList.stream()
//               .filter(user -> user.getId().equals(id))
//               .findFirst()
//               .orElse(null);
        
        return userRepository.findById(id).map(this::maptoUserResponse);
    }

    public boolean updateUser(String id, UserRequest userRequest)
    {
//        return userList.stream()
//                .filter(u -> u.getId().equals(id))
//                .findFirst()
//                .map(u -> {
//                    u.setFirstName(user.getFirstName());
//                    u.setLastName(user.getLastName());
//                    return true;
//                })
//                .orElse(false);

        return userRepository.findById(id)
                .map(existingUser->{
                    updateUserFromRequest(existingUser,userRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if(userRequest.getAddressDto() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddressDto().getStreet());
            address.setCity(userRequest.getAddressDto().getCity());
            address.setState(userRequest.getAddressDto().getState());
            address.setZipcode(userRequest.getAddressDto().getZipcode());
            address.setCountry(userRequest.getAddressDto().getCountry());
            user.setAddress(address);
        }
    }

    private UserResponse maptoUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());

        if(user.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setStreet(user.getAddress().getStreet());
            addressDto.setCity(user.getAddress().getCity());
            addressDto.setState(user.getAddress().getState());
            addressDto.setZipcode(user.getAddress().getZipcode());
            addressDto.setCountry(user.getAddress().getCountry());
            userResponse.setAddressDto(addressDto);
        }

        return userResponse;
    }
}
