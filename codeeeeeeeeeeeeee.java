

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Groups implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long groupId;

    @NotNull
    private String groupName;

    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Users groupCreator;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> members;
}


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class GroupMember implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    private boolean isAdmin;
}

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Long> {

    @Query("SELECT g FROM Groups g WHERE g.groupCreator.userId = :userId")
    List<Groups> findGroupsByCreator(Long userId);

    @Query("SELECT g FROM Groups g JOIN g.members gm WHERE gm.user.userId = :userId")
    List<Groups> findGroupsByMember(Long userId);

    @Query("SELECT g FROM Groups g WHERE LOWER(g.groupName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Groups> searchGroupsByName(String name);

    @Transactional
    void deleteByGroupCreator_UserIdAndGroupId(Long userId, Long groupId);
}


import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    void deleteByUser_UserIdAndGroup_GroupId(Long userId, Long groupId);
}


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public Groups createGroup(Long creatorId, String groupName, boolean isPublic) {
        Users creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Groups group = new Groups();
        group.setGroupCreator(creator);
        group.setGroupName(groupName);
        group.setPublic(isPublic);
        group = groupRepository.save(group);

        // Add creator as a default member
        GroupMember member = new GroupMember();
        member.setUser(creator);
        member.setGroup(group);
        member.setAdmin(true);
        groupMemberRepository.save(member);

        return group;
    }

    public List<Groups> getGroupsByCreator(Long userId) {
        return groupRepository.findGroupsByCreator(userId);
    }

    public List<Groups> getGroupsByMember(Long userId) {
        return groupRepository.findGroupsByMember(userId);
    }

    public List<Groups> searchGroups(String groupName) {
        return groupRepository.searchGroupsByName(groupName);
    }

    @Transactional
    public void deleteGroup(Long userId, Long groupId) {
        groupRepository.deleteByGroupCreator_UserIdAndGroupId(userId, groupId);
    }

    @Transactional
    public void addUserToGroup(Long groupId, Long userId, boolean isAdmin) {
        Groups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        GroupMember member = new GroupMember();
        member.setUser(user);
        member.setGroup(group);
        member.setAdmin(isAdmin);
        groupMemberRepository.save(member);
    }

    @Transactional
    public void removeUserFromGroup(Long groupId, Long userId) {
        groupMemberRepository.deleteByUser_UserIdAndGroup_GroupId(userId, groupId);
    }
}

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<Groups> createGroup(@RequestParam Long creatorId,
                                              @RequestParam String groupName,
                                              @RequestParam boolean isPublic) {
        Groups group = groupService.createGroup(creatorId, groupName, isPublic);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/by-creator/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByCreator(@PathVariable Long userId) {
        return ResponseEntity.ok(groupService.getGroupsByCreator(userId));
    }

    @GetMapping("/by-member/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByMember(@PathVariable Long userId) {
        return ResponseEntity.ok(groupService.getGroupsByMember(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Groups>> searchGroups(@RequestParam String name) {
        return ResponseEntity.ok(groupService.searchGroups(name));
    }

    @DeleteMapping("/delete/{userId}/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        groupService.deleteGroup(userId, groupId);
        return ResponseEntity.ok("Group deleted successfully");
    }

    @PostMapping("/{groupId}/add-user/{userId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long groupId,
                                                 @PathVariable Long userId,
                                                 @RequestParam boolean isAdmin) {
        groupService.addUserToGroup(groupId, userId, isAdmin);
        return ResponseEntity.ok("User added to group");
    }

    @DeleteMapping("/{groupId}/remove-user/{userId}")
    public ResponseEntity<String> removeUserFromGroup(@PathVariable Long groupId,
                                                      @PathVariable Long userId) {
        groupService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.ok("User removed from group");
    }
}


