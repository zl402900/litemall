package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallCartMapper;
import org.linlinjava.litemall.db.domain.LitemallCart;
import org.linlinjava.litemall.db.domain.LitemallCartExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LitemallCartService {
    @Resource
    private LitemallCartMapper cartMapper;

    public LitemallCart queryExist(Integer goodsId, Integer productId, Integer userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andProductIdEqualTo(productId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectOneByExample(example);
    }

    public void add(LitemallCart cart) {
        cart.setAddTime(LocalDateTime.now());
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.insertSelective(cart);
    }

    public int updateById(LitemallCart cart) {
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByPrimaryKeySelective(cart);
    }

    public List<LitemallCart> queryByUid(int userId,String scan) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false).andUserScanEqualTo(scan);
        return cartMapper.selectByExample(example);
    }


    public List<LitemallCart> queryByUidAndChecked(Integer userId,String scan) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false).andUserScanEqualTo(scan);
        return cartMapper.selectByExample(example);
    }

    public int delete(List<Integer> productIdList, int userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(productIdList);
        return cartMapper.logicalDeleteByExample(example);
    }

    public LitemallCart findById(Integer id) {
        return cartMapper.selectByPrimaryKey(id);
    }

    public int updateCheck(Integer userId, List<Integer> idsList, Boolean checked) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(idsList).andDeletedEqualTo(false);
        LitemallCart cart = new LitemallCart();
        cart.setChecked(checked);
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByExampleSelective(cart, example);
    }

    public void clearGoods(Integer userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true);
        LitemallCart cart = new LitemallCart();
        cart.setDeleted(true);
        cartMapper.updateByExampleSelective(cart, example);
    }

    public List<LitemallCart> querySelective(Integer userId, Integer goodsId, Integer page, Integer limit, String sort, String order) {
        LitemallCartExample example = new LitemallCartExample();
        LitemallCartExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return cartMapper.selectByExample(example);
    }

    public int countSelective(Integer userId, Integer goodsId, Integer page, Integer limit, String sort, String order) {
        LitemallCartExample example = new LitemallCartExample();
        LitemallCartExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (goodsId != null) {
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        return (int) cartMapper.countByExample(example);
    }

    public void deleteById(Integer id) {
        cartMapper.logicalDeleteByPrimaryKey(id);
    }

    public boolean checkExist(Integer goodsId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andCheckedEqualTo(true);
        return cartMapper.countByExample(example) != 0;
    }


    /*
    *  通过扫描条形码加入购物车 判断购物车是否已经存在该商品
    */

    public LitemallCart queryCartExist(Integer goodsId, Integer userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.queryCartExist(example);
    }

    /*
     * 通过扫描条形码获得litemall_goods_product表中的id值
    */

    public  Integer queryId(String goodSn){
       return cartMapper.queryId(goodSn);
    }

}
