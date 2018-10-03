package com.fs.eiim.restful.vo.account;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.Person;
import com.fs.eiim.restful.vo.person.PersonInfoVO;
import com.fs.eiim.restful.vo.role.RoleInfoVO;
import com.fs.eiim.service.BaseDataService;
import org.mx.comps.rbac.dal.entity.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccountInfoVO {
    private String id, code, nickname, eiimCode, avatar;
    private boolean valid = false;
    private PersonInfoVO person;
    private List<RoleInfoVO> roles;

    public static AccountInfoVO valueOf(Account account) {
        if (account == null) {
            return null;
        }
        AccountInfoVO accountInfoVO = new AccountInfoVO();
        accountInfoVO.id = account.getId();
        accountInfoVO.code = account.getCode();
        accountInfoVO.nickname = account.getNickName();
        accountInfoVO.eiimCode = account.getEiimCode();
        accountInfoVO.avatar = account.getAvatar();
        accountInfoVO.valid = account.isValid();
        // 处理人员信息
        Person person = account.getPerson();
        if (person != null) {
            accountInfoVO.person = PersonInfoVO.valueOf(
                    BaseDataService.PersonAccountTuple.valueOf(person, null, null)
            );
        }
        // 处理账户角色
        Set<Role> roles = account.getRoles();
        if (roles != null && !roles.isEmpty()) {
            List<RoleInfoVO> roleInfoVOS = new ArrayList<>();
            roles.forEach(role -> roleInfoVOS.add(RoleInfoVO.valueOf(role)));
        }
        return accountInfoVO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEiimCode() {
        return eiimCode;
    }

    public void setEiimCode(String eiimCode) {
        this.eiimCode = eiimCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public PersonInfoVO getPerson() {
        return person;
    }

    public void setPerson(PersonInfoVO person) {
        this.person = person;
    }

    public List<RoleInfoVO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleInfoVO> roles) {
        this.roles = roles;
    }
}
